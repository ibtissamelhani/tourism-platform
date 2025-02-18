package org.ibtissam.dadesadventures.DTO.Reservation;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.ibtissam.dadesadventures.domain.enums.ReservationState;
import org.ibtissam.dadesadventures.validation.EnumValue;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Activity ID is required")
    private UUID activityId;

    @Positive(message = "Number of participants must be a positive value")
    private Integer numberOfParticipants;


    @Future(message = "Reservation date must be in the future")
    private LocalDateTime reservationDate;

    @EnumValue(enumClass = ReservationState.class, message = "Reservation state")
    private String state;
}
