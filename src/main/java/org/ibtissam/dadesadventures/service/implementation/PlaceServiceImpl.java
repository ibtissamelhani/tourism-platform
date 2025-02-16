package org.ibtissam.dadesadventures.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Place.PlaceDTOMapper;
import org.ibtissam.dadesadventures.DTO.Place.PlaceRequest;
import org.ibtissam.dadesadventures.DTO.Place.PlaceResponse;
import org.ibtissam.dadesadventures.domain.entities.Place;
import org.ibtissam.dadesadventures.domain.entities.PlaceType;
import org.ibtissam.dadesadventures.exception.place.PlaceNotFoundException;
import org.ibtissam.dadesadventures.repository.PlaceRepository;
import org.ibtissam.dadesadventures.service.PlaceService;
import org.ibtissam.dadesadventures.service.PlaceTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceTypeService placeTypeService;
    private final PlaceDTOMapper placeDTOMapper;


    @Override
    public PlaceResponse createPlace(PlaceRequest placeRequest) {
        PlaceType placeType = placeTypeService.findById(placeRequest.getTypeId());

        Place place = Place.builder()
                .name(placeRequest.getName())
                .address(placeRequest.getAddress())
                .city(placeRequest.getCity())
                .type(placeType)
                .build();

        Place savedPlace = placeRepository.save(place);
        return placeDTOMapper.toResponse(savedPlace);
    }

    public Page<PlaceResponse> getAllPlaces(Pageable pageable) {
        return placeRepository.findAll(pageable)
                .map(placeDTOMapper::toResponse);
    }

    @Override
    public PlaceResponse getPlaceById(UUID id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found with ID: " + id));
        return placeDTOMapper.toResponse(place);
    }
    public Place findById(UUID id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found with ID: " + id));
    }

    @Override
    public PlaceResponse updatePlace(UUID id, PlaceRequest placeRequest) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found with ID: " + id));

        PlaceType placeType = placeTypeService.findById(placeRequest.getTypeId());

        place.setName(placeRequest.getName());
        place.setAddress(placeRequest.getAddress());
        place.setCity(placeRequest.getCity());
        place.setType(placeType);

        Place updatedPlace = placeRepository.save(place);
        return placeDTOMapper.toResponse(updatedPlace);
    }

    @Override
    public void deletePlace(UUID id) {
        if (!placeRepository.existsById(id)) {
            throw new PlaceNotFoundException("Place not found with ID: " + id);
        }
        placeRepository.deleteById(id);
    }


    @Override
    public List<PlaceResponse> searchPlacesByName(String name) {
        List<Place> places = placeRepository.findByNameContainingIgnoreCase(name);
        return places.stream()
                .map(placeDTOMapper::toResponse)
                .toList();
    }
}
