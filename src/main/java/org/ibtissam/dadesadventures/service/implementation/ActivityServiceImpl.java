package org.ibtissam.dadesadventures.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityDTOMapper;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityRequest;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityResponse;
import org.ibtissam.dadesadventures.domain.entities.*;
import org.ibtissam.dadesadventures.exception.ActivityNotFoundException;
import org.ibtissam.dadesadventures.repository.ActivityImageRepository;
import org.ibtissam.dadesadventures.repository.ActivityRepository;
import org.ibtissam.dadesadventures.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final CategoryService categoryService;
    private final PlaceService placeService;
    private final UserService userService;
    private final ActivityDTOMapper activityMapper;
    private final ActivityImageRepository activityImageRepository;

    @Override
    public ActivityResponse createActivity(ActivityRequest request) {
        Category category = categoryService.findById(request.getCategoryId());

        Place place = placeService.findById(request.getPlaceId());

        User guide = null;
        if (request.getGuideId() != null) {
            guide = userService.findById(request.getGuideId());
        }
        Activity activity = Activity.builder()
                .name(request.getName())
                .date(request.getDate())
                .category(category)
                .place(place)
                .guide(guide)
                .availability(true)
                .capacity(request.getCapacity())
                .description(request.getDescription())
                .price(request.getPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .images(new ArrayList<>())
                .build();


        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            request.getImageUrls().forEach(url -> {
                ActivityImage image = ActivityImage.builder()
                        .imageUrl(url)
                        .activity(activity)
                        .build();
                activity.getImages().add(image);
            });
        }

        Activity savedActivity = activityRepository.save(activity);

        return activityMapper.toResponse(savedActivity);
    }

    @Override
    public Page<ActivityResponse> getAllActivities(Pageable pageable) {
        return activityRepository.findAll(pageable)
                .map(activityMapper::toResponse);
    }

    @Override
    public void deleteActivity(UUID id) {
        if (!activityRepository.existsById(id)) {
            throw new ActivityNotFoundException("Activity not found with ID: " + id);
        }
        activityRepository.deleteById(id);
    }

    @Override
    public ActivityResponse updateActivity(UUID id, ActivityRequest request) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found with ID: " + id));

        // Retrieve associated entities
        Category category = categoryService.findById(request.getCategoryId());
        Place place = placeService.findById(request.getPlaceId());

        User guide = null;
        if (request.getGuideId() != null) {
            guide = userService.findById(request.getGuideId());
        }

        activity.setName(request.getName());
        activity.setDescription(request.getDescription());
        activity.setCapacity(request.getCapacity());
        activity.setPrice(request.getPrice());
        activity.setDate(request.getDate());
        activity.setAvailability(request.getAvailability());
        activity.setCategory(category);
        activity.setPlace(place);
        activity.setGuide(guide);
        activity.setUpdatedAt(LocalDateTime.now());

        // Update images if image URLs are provided
        if (request.getImageUrls() != null) {
            activity.getImages().clear();
            request.getImageUrls().forEach(url -> {
                ActivityImage image = ActivityImage.builder()
                        .imageUrl(url)
                        .activity(activity)
                        .build();
                activity.getImages().add(image);
            });
        }

        // Save the updated activity
        Activity updatedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(updatedActivity);
    }



}
