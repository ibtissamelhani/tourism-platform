package org.ibtissam.dadesadventures.DTO.Activity;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Date is required")
    @Future(message = "Date must be in the future")
    private LocalDateTime date;

    @NotNull(message = "Deadline date is required")
    @Future(message = "Deadline must be in the future")
    private LocalDateTime registrationDeadline;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotNull(message = "Place ID is required")
    private UUID placeId;

    private UUID guideId;

    private Boolean availability;

    private List<String> imageUrls;

    @AssertTrue(message = "Registration deadline must be before the activity date")
    public boolean isDeadlineValid() {
        return registrationDeadline == null || date == null || registrationDeadline.isBefore(date);
    }
}
