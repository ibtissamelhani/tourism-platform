package org.ibtissam.dadesadventures.DTO.Activity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ActivityResponse {
    private UUID id;
    private String name;
    private String description;
    private Integer capacity;
    private Double price;
    private LocalDateTime date;
    private Boolean availability;
    private String categoryName;
    private String placeName;
    private String guideName;
    private List<String> imageUrls;
}
