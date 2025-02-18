package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.Reservation.ReservationRequest;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationResponse;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest reservationRequest);
}
