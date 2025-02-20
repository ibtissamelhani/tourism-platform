package org.ibtissam.dadesadventures.web.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationRequest;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationResponse;
import org.ibtissam.dadesadventures.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping
    public ResponseEntity<Page<ReservationResponse>> getAllReservations(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<ReservationResponse> reservations = reservationService.getAllReservations(pageable);
        return ResponseEntity.ok(reservations);
    }
}
