package be.winagent.weba2.domain.types;

import be.winagent.weba2.domain.repositories.EventRepository;
import com.vladmihalcea.hibernate.type.ImmutableType;
import com.vladmihalcea.hibernate.type.util.ReflectionUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.function.Function;

import org.postgresql.util.PGobject;

public class PostgresqlTsTzRangeType extends ImmutableType<TsTzRange> {
    public static final DateTimeFormatter ZONE_DATE_TIME = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .optionalStart()
            .appendPattern(".")
            .appendFraction(ChronoField.NANO_OF_SECOND, 1, 6, false)
            .optionalEnd()
            .appendPattern("X")
            .toFormatter();
    public static final String EMPTY = "empty";
    public static final String INFINITY = "infinity";

    public String asString(TsTzRange range) {
        return String.format("[%s,%s]", ZONE_DATE_TIME.format(range.getStart()), ZONE_DATE_TIME.format(range.getEnd()));
    }

    public PostgresqlTsTzRangeType() {
        super(TsTzRange.class);
    }

    @Override
    protected TsTzRange get(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
        Object pgObject = rs.getObject(names[0]);
        String value = ReflectionUtils.invokeGetter(
                pgObject,
                "value"
        );

        return ofString(value);
    }

    @Override
    protected void set(PreparedStatement st, TsTzRange range, int index, SharedSessionContractImplementor session) throws SQLException {
        if (range == null) {
            st.setNull(index, Types.OTHER);
        } else {
            PGobject object = new PGobject();
            object.setType("tstzrange");
            object.setValue(asString(range));

            st.setObject(index, object);
        }
    }

    @Override
    public int[] sqlTypes() {
        return new int[0];
    }


    private static Function<String, String> unquote() {
        return s -> {
            if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"') {
                return s.substring(1, s.length() - 1);
            }

            return s;
        };
    }



    private static Function<String, ZonedDateTime> parseZonedDateTime() {
        return s -> {
            try {
                return ZonedDateTime.parse(s, ZONE_DATE_TIME);
            } catch (DateTimeParseException e) {
                return ZonedDateTime.parse(s);
            }
        };
    }

    public static TsTzRange ofString(String str) {
        Function<String, ZonedDateTime> converter = parseZonedDateTime().compose(unquote());
        if (str.charAt(0) != '[' || str.charAt(str.length() - 1) != ']') {
            throw new IllegalArgumentException("Not a finite closed range");
        }

        int delim = str.indexOf(',');
        if (delim == -1) {
            throw new IllegalArgumentException("Cannot find comma character");
        }

        String lowerStr = str.substring(1, delim);
        String upperStr = str.substring(delim + 1, str.length() - 1);
        if (lowerStr.length() == 0 || lowerStr.endsWith(INFINITY) || upperStr.length() == 0 || upperStr.endsWith(INFINITY)) {
            throw new IllegalArgumentException("Not a finite closed range");
        }

        ZonedDateTime lower = converter.apply(lowerStr);
        ZonedDateTime upper = converter.apply(upperStr);

        TsTzRange range = new TsTzRange(lower, upper);

        ZoneId lowerZone = range.getStart().getZone();
        ZoneId upperZone = range.getEnd().getZone();
        if (!lowerZone.equals(upperZone)) {
            Duration lowerDst = ZoneId.systemDefault().getRules().getDaylightSavings(range.getStart().toInstant());
            Duration upperDst = ZoneId.systemDefault().getRules().getDaylightSavings(range.getEnd().toInstant());
            long dstSeconds = upperDst.minus(lowerDst).getSeconds();
            if (dstSeconds < 0) {
                dstSeconds *= -1;
            }
            long zoneDriftSeconds = ((ZoneOffset) lowerZone).getTotalSeconds() - ((ZoneOffset) upperZone).getTotalSeconds();
            if (zoneDriftSeconds < 0) {
                zoneDriftSeconds *= -1;
            }

            if (dstSeconds != zoneDriftSeconds) {
                throw new IllegalArgumentException("The upper and lower bounds must be in same time zone!");
            }
        }

        return range;
    }
}
