package org.ibtissam.dadesadventures.service.implementation;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationDTOMapper;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationRequest;
import org.ibtissam.dadesadventures.DTO.Reservation.ReservationResponse;
import org.ibtissam.dadesadventures.domain.entities.Activity;
import org.ibtissam.dadesadventures.domain.entities.Reservation;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.ibtissam.dadesadventures.domain.enums.ReservationState;
import org.ibtissam.dadesadventures.exception.activity.ActivityNotAvailableException;
import org.ibtissam.dadesadventures.exception.reservation.*;
import org.ibtissam.dadesadventures.repository.ReservationRepository;
import org.ibtissam.dadesadventures.service.ActivityService;
import org.ibtissam.dadesadventures.service.ReservationService;
import org.ibtissam.dadesadventures.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final ActivityService activityService;
    private final ReservationDTOMapper reservationDTOMapper;
    private final MailServiceImpl emailService;
    private final PaymentServiceImpl paymentService;


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

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(activity.getRegistrationDeadline())) {
            throw new RegistrationClosedException("Registration is closed for this activity. The deadline was: " + activity.getRegistrationDeadline());
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

        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(
                    (long) (reservation.getTotalPrice() * 100),
                    "usd",
                    "Reservation for " + activity.getName()
            );
            reservation.setPaymentId(paymentIntent.getId());
        } catch (StripeException e) {
            throw new PaymentFailedException("Payment failed: " + e.getMessage());
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        sendReservationConfirmationEmail(user, activity, savedReservation);
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

    @Override
    public void deleteReservation(UUID id) {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationNotFoundException("Reservation not found with ID: " + id);
        }
        reservationRepository.deleteById(id);
    }

    private void sendReservationConfirmationEmail(User user, Activity activity, Reservation reservation) {
        String subject = "Reservation Confirmation";

        // Create a Thymeleaf context and set variables
        Context context = new Context();
        context.setVariable("userFirstName", user.getFirstName());
        context.setVariable("activityName", activity.getName());
        context.setVariable("numberOfParticipants", reservation.getNumberOfParticipants());
        context.setVariable("totalPrice", reservation.getTotalPrice());
        context.setVariable("reservationDate", reservation.getReservationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        try {
            emailService.sendHtmlEmail(user.getEmail(), subject, "reservation-email-template", context);
        } catch (MessagingException e) {
            throw new FailedToSendEmailException("Failed to send email");
        }
    }
}
