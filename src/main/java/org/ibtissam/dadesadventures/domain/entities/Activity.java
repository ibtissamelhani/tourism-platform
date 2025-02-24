package org.ibtissam.dadesadventures.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.ibtissam.dadesadventures.domain.enums.ActivityStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Boolean availability;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne
    @JoinColumn(name = "guide_id", nullable = true)
    private User guide;

    @Column(nullable = false)
    private LocalDateTime registrationDeadline;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityImage> images = new ArrayList<>();

    @Column(nullable = false)
    private ActivityStatus status;

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
}