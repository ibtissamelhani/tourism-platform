package org.ibtissam.dadesadventures.DTO.Activity;

import org.ibtissam.dadesadventures.domain.entities.Activity;
import org.ibtissam.dadesadventures.domain.entities.ActivityImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityDTOMapper {

    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "place.id", source = "placeId")
    @Mapping(target = "guide.id", source = "guideId", ignore = true)
    Activity toEntity(ActivityRequest request);


    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "place", source = "place.name")
    @Mapping(target = "guideEmail", source = "guide.email", defaultValue = "No Guide Assigned")
    @Mapping(target = "guideFirstName", source = "guide.firstName", defaultValue = "No Guide Assigned")
    @Mapping(target = "guideLastName", source = "guide.lastName", defaultValue = "No Guide Assigned")
    @Mapping(target = "imageUrls", expression = "java(activity.getImages().stream().map(img -> img.getImageUrl()).toList())")
    ActivityResponse toResponse(Activity activity);



}
