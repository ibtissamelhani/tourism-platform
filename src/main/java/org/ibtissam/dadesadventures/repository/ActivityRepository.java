package org.ibtissam.dadesadventures.repository;

import org.ibtissam.dadesadventures.domain.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ActivityRepository  extends JpaRepository<Activity, UUID>, ActivitySearchRepository {

    @Query("SELECT COUNT(a) > 0 FROM Activity a WHERE a.guide.id = :guideId AND a.date = :date")
    boolean isGuideBusy(@Param("guideId") UUID guideId, @Param("date") LocalDateTime date);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Activity a " +
            "WHERE a.id = :activityId AND a.date > :limitDate")
    boolean isActivityOpenForRegistration(@Param("activityId") UUID activityId,
                                          @Param("limitDate") LocalDateTime limitDate);
}
