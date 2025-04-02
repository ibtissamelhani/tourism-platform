package org.ibtissam.dadesadventures.repository;

import org.ibtissam.dadesadventures.domain.entities.Reservation;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByActivityId(UUID activityId);
    Optional<Reservation> findByUserIdAndActivityId(UUID userId, UUID activityId);
    List<Reservation> findByUser(User user);


}
