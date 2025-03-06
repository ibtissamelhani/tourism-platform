package org.ibtissam.dadesadventures.repository;

import org.ibtissam.dadesadventures.domain.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlaceRepository extends JpaRepository<Place, UUID> {
    List<Place> findByNameContainingIgnoreCase(String name);
}
