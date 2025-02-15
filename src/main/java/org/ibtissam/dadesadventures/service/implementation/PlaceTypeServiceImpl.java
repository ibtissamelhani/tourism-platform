package org.ibtissam.dadesadventures.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.domain.entities.PlaceType;
import org.ibtissam.dadesadventures.exception.place.TypeAlreadyExistException;
import org.ibtissam.dadesadventures.exception.place.TypeNotFoundException;
import org.ibtissam.dadesadventures.repository.PlaceTypeRepository;
import org.ibtissam.dadesadventures.service.PlaceTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceTypeServiceImpl implements PlaceTypeService {
    private final PlaceTypeRepository typeRepository;

    public List<PlaceType> findAll() {
        return typeRepository.findAll();
    }

    @Override
    public PlaceType findById(UUID id) {
        return typeRepository.findById(id)
                .orElseThrow(() -> new TypeNotFoundException("Type not found."));
    }

    @Override
    public PlaceType create(PlaceType type) {
        if (typeRepository.existsByName(type.getName())) {
            throw new TypeAlreadyExistException("type with name '" + type.getName() + "' already exists.");
        }
        return typeRepository.save(type);
    }

    @Override
    public PlaceType update(UUID id, PlaceType typeRequest) {
        PlaceType existingType = findById(id);
        if (typeRepository.existsByName(typeRequest.getName())) {
            throw new TypeAlreadyExistException("type with name '" + typeRequest.getName() + "' already exists.");
        }
        existingType.setName(typeRequest.getName());
        return typeRepository.save(existingType);
    }

    @Override
    public void delete(UUID id) {
        PlaceType existingType = findById(id);
        typeRepository.delete(existingType);
    }
}
