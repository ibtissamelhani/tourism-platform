package org.ibtissam.dadesadventures.DTO.Activity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityResponse {
    private UUID id;
    private String name;
    private String description;
    private Integer capacity;
    private Double price;
    private LocalDateTime date;
    private LocalDateTime registrationDeadline;
    private Boolean availability;
    private String category;
    private String place;
    private String guideFirstName;
    private String guideLastName;
    private String guideEmail;
    private List<String> imageUrls;
}
