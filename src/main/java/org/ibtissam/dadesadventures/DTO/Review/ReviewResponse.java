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
    private User user;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
