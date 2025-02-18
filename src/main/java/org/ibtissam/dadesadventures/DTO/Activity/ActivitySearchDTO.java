package org.ibtissam.dadesadventures.DTO.Activity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class ActivitySearchDTO {
    private String name;
    private UUID categoryId;
    private UUID placeId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
