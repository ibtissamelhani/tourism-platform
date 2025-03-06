package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.domain.entities.PlaceType;

import java.util.List;
import java.util.UUID;

public interface PlaceTypeService {
    List<PlaceType> findAll();
    PlaceType findById(UUID id);
    PlaceType create(PlaceType type);
    PlaceType update(UUID id, PlaceType typeRequest);
    void delete(UUID id);

}
