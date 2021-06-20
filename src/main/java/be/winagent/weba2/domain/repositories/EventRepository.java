package be.winagent.weba2.domain.repositories;

import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Location;
import be.winagent.weba2.domain.types.PostgresqlTsTzRangeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends CrudRepository<Event, UUID> {


    default Optional<Event> getEventByLocationAndTime(Location location, ZonedDateTime time)  {
        return findByLocationIdAndTimeRangeContains(location.getId(), PostgresqlTsTzRangeType.ZONE_DATE_TIME.format(time));
    }

    @Query(value = "SELECT * FROM event e WHERE e.location_id = ?1 AND e.time_range @> CAST (?2 as timestamptz)", nativeQuery = true)
    Optional<Event> findByLocationIdAndTimeRangeContains(UUID locationId, String time);

    default Page<Event> getFutureEventsForLocation(Pageable pageable, Location location) {
        return getEventsForLocationAfterDate(pageable, location.getId(), ZonedDateTime.now());
    }

    default Page<Event> getPastEventsForLocation(Pageable pageable, Location location) {
        return getEventsForLocationBeforeDate(pageable, location.getId(), ZonedDateTime.now());
    }

    @Query(value = "SELECT * FROM event e WHERE e.location_id = ?1 AND UPPER(e.time_range) >= CAST (?2 as timestamptz) ORDER BY e.time_range ASC", nativeQuery = true)
    Page<Event> getEventsForLocationAfterDate(Pageable pageable, UUID locationId, ZonedDateTime time);

    @Query(value = "SELECT * FROM event e WHERE e.location_id = ?1 AND LOWER(e.time_range) <= CAST (?2 as timestamptz) ORDER BY e.time_range DESC", nativeQuery = true)
    Page<Event> getEventsForLocationBeforeDate(Pageable pageable, UUID locationId, ZonedDateTime time);
}
