package org.ibtissam.dadesadventures.DTO.Place;

import org.ibtissam.dadesadventures.domain.entities.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaceDTOMapper {

    @Mapping(target = "id", ignore = true)
    Place toEntity(PlaceRequest placeRequest);

    @Mapping(target = "type", source = "type.name")
    PlaceResponse toResponse(Place place);
}
