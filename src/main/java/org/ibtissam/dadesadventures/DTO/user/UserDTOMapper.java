package org.ibtissam.dadesadventures.DTO.user;

import org.ibtissam.dadesadventures.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    @Mapping(source = "active", target = "isActive")
    UserResponse toUserDTO(User user);
    User toUser(UserRequest userRequest);
}
