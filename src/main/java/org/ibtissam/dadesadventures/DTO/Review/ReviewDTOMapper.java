package org.ibtissam.dadesadventures.DTO.Review;

import org.ibtissam.dadesadventures.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewDTOMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Review toEntity(ReviewRequest reviewRequest);

    @Mapping(target = "userFirstName", source = "user.firstName")
    @Mapping(target = "userLastName", source = "user.lastName")
    ReviewResponse toResponse(Review review);
}
