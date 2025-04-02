package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.Reservation.ReservationRequest;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationResponse;
import org.ibtissam.dadesadventures.domain.entities.Reservation;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest reservationRequest);
    List<ReservationResponse> getReservationsByActivityId(UUID activityId);
    Page<ReservationResponse> getAllReservations(Pageable pageable);
    ReservationResponse getReservationById(UUID id);
    void deleteReservation(UUID id);
    List<ReservationResponse> findByUser(User user);
}
