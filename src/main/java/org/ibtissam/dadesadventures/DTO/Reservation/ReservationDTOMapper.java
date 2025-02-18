package org.ibtissam.dadesadventures.DTO.Reservation;

import org.ibtissam.dadesadventures.domain.entities.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationDTOMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", source = "userId")
    @Mapping(target = "activity", source = "activityId")
    Reservation toEntity(ReservationRequest reservationRequest);

    @Mapping(target = "UserFirstName", source = "user.firstName")
    @Mapping(target = "UserLastName", source = "user.lastName")
    @Mapping(target = "activity", source = "activity.name")
    ReservationResponse toResponse(Reservation reservation);
}
