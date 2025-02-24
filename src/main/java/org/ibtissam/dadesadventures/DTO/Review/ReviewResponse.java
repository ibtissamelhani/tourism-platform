package org.ibtissam.dadesadventures.DTO.Review;

import lombok.*;
import org.ibtissam.dadesadventures.domain.entities.User;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private UUID id;
    private String userFirstName;
    private String userLastName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
