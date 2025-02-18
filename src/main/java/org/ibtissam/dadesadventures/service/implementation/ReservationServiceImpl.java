package org.ibtissam.dadesadventures.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationDTOMapper;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationRequest;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationResponse;
import org.ibtissam.dadesadventures.domain.entities.Activity;
import org.ibtissam.dadesadventures.domain.entities.Reservation;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.ibtissam.dadesadventures.domain.enums.ReservationState;
import org.ibtissam.dadesadventures.repository.ReservationRepository;
import org.ibtissam.dadesadventures.repository.UserRepository;
import org.ibtissam.dadesadventures.service.ActivityService;
import org.ibtissam.dadesadventures.service.ReservationService;
import org.ibtissam.dadesadventures.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
}
