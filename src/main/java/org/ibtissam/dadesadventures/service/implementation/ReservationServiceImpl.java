package org.ibtissam.dadesadventures.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationDTOMapper;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationRequest;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationResponse;
import org.ibtissam.dadesadventures.domain.entities.Activity;
import org.ibtissam.dadesadventures.domain.entities.Reservation;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.ibtissam.dadesadventures.domain.enums.ReservationState;
import org.ibtissam.dadesadventures.exception.activity.ActivityNotAvailableException;
import org.ibtissam.dadesadventures.exception.reservation.ReservationNotFoundException;
import org.ibtissam.dadesadventures.repository.ReservationRepository;
import org.ibtissam.dadesadventures.service.ActivityService;
import org.ibtissam.dadesadventures.service.ReservationService;
import org.ibtissam.dadesadventures.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final ActivityService activityService;
    private final ReservationDTOMapper reservationDTOMapper;


    @Override
    public ReservationResponse createReservation(ReservationRequest reservationRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        User userDetails = (User) authentication.getPrincipal();

        User user = userService.findById(userDetails.getId());

        Activity activity = activityService.findById(reservationRequest.getActivityId());

        if (!activity.getAvailability()){
            throw new ActivityNotAvailableException("Activity is not available");
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .activity(activity)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .numberOfParticipants(reservationRequest.getNumberOfParticipants())
                .state(ReservationState.PENDING)
                .build();

        reservation.calculateTotalPrice();

        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationDTOMapper.toResponse(savedReservation);
    }

    @Override
    public List<ReservationResponse> getReservationsByActivityId(UUID activityId) {
        List<Reservation> reservations = reservationRepository.findByActivityId(activityId);
        return reservations.stream()
                .map(reservationDTOMapper::toResponse)
                .toList();
    }

    @Override
    public Page<ReservationResponse> getAllReservations(Pageable pageable) {
        Page<Reservation> reservations = reservationRepository.findAll(pageable);
        return reservations.map(reservationDTOMapper::toResponse);
    }

    @Override
    public ReservationResponse getReservationById(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with ID: " + id));
        return reservationDTOMapper.toResponse(reservation);
    }

}
