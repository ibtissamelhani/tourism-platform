package org.ibtissam.dadesadventures.DTO.Reservation;

import lombok.*;
import org.ibtissam.dadesadventures.domain.enums.ReservationState;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private UUID id;
    private String UserFirstName;
    private String UserLastName;
    private String activity;
    private Integer numberOfParticipants;
    private Double totalPrice;
    private LocalDateTime reservationDate;
    private ReservationState state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
