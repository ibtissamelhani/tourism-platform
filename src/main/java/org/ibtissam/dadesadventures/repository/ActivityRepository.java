package org.ibtissam.dadesadventures.repository;

import org.ibtissam.dadesadventures.domain.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivityRepository  extends JpaRepository<Activity, UUID>, ActivitySearchRepository {
}
