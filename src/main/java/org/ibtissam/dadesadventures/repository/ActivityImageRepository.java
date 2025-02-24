package org.ibtissam.dadesadventures.repository;

import org.ibtissam.dadesadventures.domain.entities.ActivityImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivityImageRepository extends JpaRepository<ActivityImage, UUID> {

}
