package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.domain.entities.PlaceType;
import org.ibtissam.dadesadventures.exception.place.TypeAlreadyExistException;
import org.ibtissam.dadesadventures.exception.place.TypeNotFoundException;
import org.ibtissam.dadesadventures.repository.PlaceTypeRepository;
import org.ibtissam.dadesadventures.service.implementation.PlaceTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaceTypeServiceImplTest {

    @Mock
    private PlaceTypeRepository typeRepository;

    @InjectMocks
    private PlaceTypeServiceImpl placeTypeService;

    private PlaceType placeType;
    private UUID typeId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        typeId = UUID.randomUUID();
        placeType = PlaceType.builder()
                .id(typeId)
                .name("Hotel")
                .build();
    }

    @Test
    void PlaceTypeService_findAll_returnsListOfAllTypes() {
        // Given
        List<PlaceType> expectedList = List.of(
                PlaceType.builder().id(UUID.randomUUID()).name("Hotel").build(),
                PlaceType.builder().id(UUID.randomUUID()).name("Restaurant").build()
        );
        when(typeRepository.findAll()).thenReturn(expectedList);

        // When
        List<PlaceType> resultedList = placeTypeService.findAll();

        // Then
        verify(typeRepository).findAll();
        assertEquals(expectedList.size(), resultedList.size());
        assertEquals(expectedList, resultedList);
    }

    @Test
    void PlaceTypeService_findById_returnsType_whenFound() {
        // Given
        when(typeRepository.findById(typeId)).thenReturn(Optional.of(placeType));

        // When
        PlaceType resultedType = placeTypeService.findById(typeId);

        // Then
        verify(typeRepository).findById(typeId);
        assertEquals(placeType, resultedType);
    }

    @Test
    void PlaceTypeService_findById_throwsTypeNotFoundException_whenNotFound() {
        // Given
        UUID randomId = UUID.randomUUID();
        when(typeRepository.findById(randomId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(TypeNotFoundException.class, () -> placeTypeService.findById(randomId));
        assertEquals("Type not found.", exception.getMessage());
        verify(typeRepository).findById(randomId);
    }

    @Test
    void PlaceTypeService_create_returnsType_whenSuccessful() {
        // Given
        when(typeRepository.existsByName(placeType.getName())).thenReturn(false);
        when(typeRepository.save(placeType)).thenReturn(placeType);

        // When
        PlaceType resultedType = placeTypeService.create(placeType);

        // Then
        verify(typeRepository).existsByName(placeType.getName());
        verify(typeRepository).save(placeType);
        assertEquals(placeType, resultedType);
    }

    @Test
    void PlaceTypeService_create_throwsTypeAlreadyExistException_whenAlreadyExists() {
        // Given
        when(typeRepository.existsByName(placeType.getName())).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(TypeAlreadyExistException.class,
                () -> placeTypeService.create(placeType));
        assertTrue(exception.getMessage().contains("type with name '" + placeType.getName() + "' already exists."));
        verify(typeRepository).existsByName(placeType.getName());
        verify(typeRepository, never()).save(any(PlaceType.class));
    }

    @Test
    void PlaceTypeService_update_returnsUpdatedType_whenSuccessful() {
        // Given
        UUID id = placeType.getId();
        PlaceType updateRequest = PlaceType.builder().name("Updated Type").build();
        PlaceType existingType = PlaceType.builder().id(id).name("Old Type").build();
        PlaceType updatedType = PlaceType.builder().id(id).name(updateRequest.getName()).build();

        when(typeRepository.findById(id)).thenReturn(Optional.of(existingType));
        when(typeRepository.existsByName(updateRequest.getName())).thenReturn(false);
        when(typeRepository.save(existingType)).thenReturn(updatedType);

        // When
        PlaceType resultedType = placeTypeService.update(id, updateRequest);

        // Then
        verify(typeRepository).findById(id);
        verify(typeRepository).existsByName(updateRequest.getName());
        verify(typeRepository).save(existingType);
        assertEquals(updateRequest.getName(), resultedType.getName());
    }

    @Test
    void PlaceTypeService_update_throwsTypeAlreadyExistException_whenNameAlreadyExists() {
        // Given
        UUID id = placeType.getId();
        PlaceType existingType = PlaceType.builder().id(id).name("Old Type").build();
        PlaceType updateRequest = PlaceType.builder().name("Existing Type").build();

        when(typeRepository.findById(id)).thenReturn(Optional.of(existingType));
        when(typeRepository.existsByName(updateRequest.getName())).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(TypeAlreadyExistException.class, () -> placeTypeService.update(id, updateRequest));
        assertTrue(exception.getMessage().contains("type with name '" + updateRequest.getName() + "' already exists."));
        verify(typeRepository).findById(id);
        verify(typeRepository).existsByName(updateRequest.getName());
        verify(typeRepository, never()).save(any(PlaceType.class));
    }

    @Test
    void PlaceTypeService_delete_deletesType_whenFound() {
        // Given
        UUID id = placeType.getId();
        when(typeRepository.findById(id)).thenReturn(Optional.of(placeType));

        // When
        placeTypeService.delete(id);

        // Then
        verify(typeRepository).findById(id);
        verify(typeRepository).delete(placeType);
    }
}