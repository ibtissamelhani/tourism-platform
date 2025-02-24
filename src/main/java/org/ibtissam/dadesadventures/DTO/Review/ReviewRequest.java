package org.ibtissam.dadesadventures.DTO.Review;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ReviewRequest {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;
}
