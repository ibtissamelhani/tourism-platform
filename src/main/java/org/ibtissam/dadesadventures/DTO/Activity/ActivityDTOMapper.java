package org.ibtissam.dadesadventures.DTO.Activity;

import org.ibtissam.dadesadventures.domain.entities.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActivityDTOMapper {

    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "place.id", source = "placeId")
    @Mapping(target = "guide.id", source = "guideId", ignore = true) // Guide is optional
    Activity toEntity(ActivityRequest request);

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "placeName", source = "place.name")
    @Mapping(target = "guideName", source = "guide.email", defaultValue = "No Guide Assigned")
    ActivityResponse toResponse(Activity activity);

}
