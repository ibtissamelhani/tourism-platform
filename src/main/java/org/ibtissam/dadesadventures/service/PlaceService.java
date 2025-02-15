package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.Place.PlaceRequest;
import org.ibtissam.dadesadventures.DTO.Place.PlaceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PlaceService {
    PlaceResponse createPlace(PlaceRequest placeRequest);
    PlaceResponse getPlaceById(UUID id);
    PlaceResponse updatePlace(UUID id, PlaceRequest placeRequest);
    void deletePlace(UUID id);
    Page<PlaceResponse> getAllPlaces(Pageable pageable);
    List<PlaceResponse> searchPlacesByName(String name);
}
