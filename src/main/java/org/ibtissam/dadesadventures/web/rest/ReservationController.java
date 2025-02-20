package org.ibtissam.dadesadventures.web.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationRequest;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationResponse;
import org.ibtissam.dadesadventures.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@AllArgsConstructor
public class ReservationController {
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest reservationRequest
    )
    {
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponse);
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByActivityId(@PathVariable UUID activityId) {
        List<ReservationResponse> reservations = reservationService.getReservationsByActivityId(activityId);
        return ResponseEntity.ok(reservations);
    }
}
