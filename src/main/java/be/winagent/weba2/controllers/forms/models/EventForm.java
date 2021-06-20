package be.winagent.weba2.controllers.forms.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class EventForm {
    @NotEmpty
    @Length(max = 255)
    private String name;

    @NotNull
    private boolean open;

    @NotNull
    private String startDate;
    @NotNull
    private LocalTime startTime;

    @NotNull
    private String endDate;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private UUID locationId;
}
