package org.ibtissam.dadesadventures.repository;

import org.ibtissam.dadesadventures.domain.entities.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaceTypeRepository extends JpaRepository<PlaceType, UUID> {
}
