package org.ibtissam.dadesadventures.DTO.Review;

import org.ibtissam.dadesadventures.domain.entities.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewResponse {

    private UUID id;
    private User userId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
