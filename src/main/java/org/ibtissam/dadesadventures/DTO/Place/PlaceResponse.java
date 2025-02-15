package org.ibtissam.dadesadventures.DTO.Place;

import lombok.*;
import org.ibtissam.dadesadventures.domain.entities.PlaceType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceResponse {
    private UUID id;
    private String name;
    private String address;
    private String city;
    private PlaceType type;
}
