package org.ibtissam.dadesadventures.DTO.Place;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "address is required")
    private String address;

    @NotBlank(message = "city is required")
    private String city;

    @NotNull(message = "typeId is required")
    private UUID typeId;
}
