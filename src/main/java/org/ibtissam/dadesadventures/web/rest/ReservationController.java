package org.ibtissam.dadesadventures.web.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationRequest;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationResponse;
import org.ibtissam.dadesadventures.domain.entities.Reservation;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.ibtissam.dadesadventures.service.ReservationService;
import org.ibtissam.dadesadventures.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@AllArgsConstructor
public class ReservationController {
    private ReservationService reservationService;
    private UserService userService;

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

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable UUID id) {
        ReservationResponse reservationResponse = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-reservations")
    public ResponseEntity<List<ReservationResponse>> getMyReservations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        User userDetails = (User) auth.getPrincipal();
        User user = userService.findById(userDetails.getId());

        return ResponseEntity.ok(reservationService.findByUser(user));
    }
}
