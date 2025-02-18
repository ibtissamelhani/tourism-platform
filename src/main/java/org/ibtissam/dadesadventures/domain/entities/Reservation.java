package org.ibtissam.dadesadventures.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.ibtissam.dadesadventures.domain.enums.ReservationState;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(nullable = false)
    private Integer numberOfParticipants;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    @Column(nullable = false)
    private ReservationState state;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    private Double calculateTotalPrice(double activityPrice) {
        totalPrice = activityPrice * numberOfParticipants;
        return totalPrice;
    }
}
