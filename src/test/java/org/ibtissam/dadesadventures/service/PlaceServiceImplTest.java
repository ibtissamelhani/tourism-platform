package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.Place.PlaceDTOMapper;
import org.ibtissam.dadesadventures.DTO.Place.PlaceRequest;
import org.ibtissam.dadesadventures.DTO.Place.PlaceResponse;
import org.ibtissam.dadesadventures.domain.entities.Place;
import org.ibtissam.dadesadventures.domain.entities.PlaceType;
import org.ibtissam.dadesadventures.exception.place.PlaceNotFoundException;
import org.ibtissam.dadesadventures.repository.PlaceRepository;
import org.ibtissam.dadesadventures.service.implementation.PlaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlaceServiceImplTest {
    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private PlaceTypeService placeTypeService;

    @Mock
    private PlaceDTOMapper placeDTOMapper;

    @InjectMocks
    private PlaceServiceImpl placeService;

    private PlaceRequest placeRequest;
    private PlaceType placeType;
    private Place place;
    private PlaceResponse placeResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        //PlaceRequest
        placeRequest = new PlaceRequest();
        placeRequest.setName("Place 1");
        placeRequest.setAddress("Address 1");
        placeRequest.setCity("City 1");
        placeRequest.setTypeId(UUID.randomUUID());

        // PlaceType
        placeType = PlaceType.builder()
                .id(placeRequest.getTypeId())
                .name("Hotel") // Exemple
                .build();

        // Place
        place = Place.builder()
                .id(UUID.randomUUID())
                .name(placeRequest.getName())
                .address(placeRequest.getAddress())
                .city(placeRequest.getCity())
                .type(placeType)
                .build();

        // Préparation PlaceResponse
        placeResponse = PlaceResponse.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .city(place.getCity())
                .type(placeType)
                .build();
    }

    @Test
    void PlaceService_createPlace_returnsPlaceResponse_whenValid() {
        // Given
        when(placeTypeService.findById(placeRequest.getTypeId())).thenReturn(placeType);
        when(placeRepository.save(any(Place.class))).thenReturn(place);
        when(placeDTOMapper.toResponse(place)).thenReturn(placeResponse);

        // When
        PlaceResponse resultedResponse = placeService.createPlace(placeRequest);

        // Then
        verify(placeTypeService).findById(placeRequest.getTypeId());
        verify(placeRepository).save(any(Place.class));
        assertEquals(placeResponse, resultedResponse);
    }

    @Test
    void PlaceService_getAllPlaces_returnsPageOfPlaceResponses() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Place> placeList = List.of(place);
        Page<Place> page = new PageImpl<>(placeList, pageable, placeList.size());
        when(placeRepository.findAll(pageable)).thenReturn(page);
        when(placeDTOMapper.toResponse(place)).thenReturn(placeResponse);

        // When
        Page<PlaceResponse> resultedPage = placeService.getAllPlaces(pageable);

        // Then
        verify(placeRepository).findAll(pageable);
        assertEquals(1, resultedPage.getTotalElements());
        assertEquals(placeResponse, resultedPage.getContent().get(0));
    }

    @Test
    void PlaceService_getPlaceById_returnsPlaceResponse_whenFound() {
        // Given
        UUID id = place.getId();
        when(placeRepository.findById(id)).thenReturn(Optional.of(place));
        when(placeDTOMapper.toResponse(place)).thenReturn(placeResponse);

        // When
        PlaceResponse resultedResponse = placeService.getPlaceById(id);

        // Then
        verify(placeRepository).findById(id);
        assertEquals(placeResponse, resultedResponse);
    }

    @Test
    void PlaceService_getPlaceById_throwsPlaceNotFoundException_whenNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        when(placeRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PlaceNotFoundException.class, () -> placeService.getPlaceById(id));
        verify(placeRepository).findById(id);
    }

    @Test
    void PlaceService_updatePlace_returnsUpdatedPlaceResponse_whenValid() {
        // Given
        UUID id = place.getId();
        PlaceRequest updateRequest = new PlaceRequest();
        updateRequest.setName("Updated Place");
        updateRequest.setAddress("Updated Address");
        updateRequest.setCity("Updated City");
        updateRequest.setTypeId(placeType.getId());

        Place updatedPlace = Place.builder()
                .id(id)
                .name(updateRequest.getName())
                .address(updateRequest.getAddress())
                .city(updateRequest.getCity())
                .type(placeType)
                .build();

        PlaceResponse updatedResponse = PlaceResponse.builder()
                .id(id)
                .name(updateRequest.getName())
                .address(updateRequest.getAddress())
                .city(updateRequest.getCity())
                .type(placeType)
                .build();

        when(placeRepository.findById(id)).thenReturn(Optional.of(place));
        when(placeTypeService.findById(updateRequest.getTypeId())).thenReturn(placeType);
        when(placeRepository.save(any(Place.class))).thenReturn(updatedPlace);
        when(placeDTOMapper.toResponse(updatedPlace)).thenReturn(updatedResponse);

        // When
        PlaceResponse resultedResponse = placeService.updatePlace(id, updateRequest);

        // Then
        verify(placeRepository).findById(id);
        verify(placeTypeService).findById(updateRequest.getTypeId());
        verify(placeRepository).save(any(Place.class));
        assertEquals(updatedResponse, resultedResponse);
    }

    @Test
    void PlaceService_deletePlace_deletesPlace_whenFound() {
        // Given
        UUID id = place.getId();
        when(placeRepository.existsById(id)).thenReturn(true);

        // When
        placeService.deletePlace(id);

        // Then
        verify(placeRepository).existsById(id);
        verify(placeRepository).deleteById(id);
    }

    @Test
    void PlaceService_deletePlace_throwsPlaceNotFoundException_whenNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        when(placeRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(PlaceNotFoundException.class, () -> placeService.deletePlace(id));
        verify(placeRepository).existsById(id);
        verify(placeRepository, never()).deleteById(any());
    }

    @Test
    void PlaceService_searchPlacesByName_returnsListOfPlaceResponses() {
        // Given
        String searchName = "Place";
        List<Place> expectedList = List.of(place);
        when(placeRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(expectedList);
        when(placeDTOMapper.toResponse(place)).thenReturn(placeResponse);

        // When
        List<PlaceResponse> resultedList = placeService.searchPlacesByName(searchName);

        // Then
        verify(placeRepository).findByNameContainingIgnoreCase(searchName);
        assertEquals(1, resultedList.size());
        assertEquals(placeResponse, resultedList.get(0));
    }
}