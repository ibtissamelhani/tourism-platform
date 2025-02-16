package org.ibtissam.dadesadventures.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityDTOMapper;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityRequest;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityResponse;
import org.ibtissam.dadesadventures.domain.entities.Activity;
import org.ibtissam.dadesadventures.domain.entities.Category;
import org.ibtissam.dadesadventures.domain.entities.Place;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.ibtissam.dadesadventures.repository.ActivityRepository;
import org.ibtissam.dadesadventures.service.*;
import org.springframework.stereotype.Service;

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

    public ActivityResponse createActivity(ActivityRequest request) {
        Category category = categoryService.findById(request.getCategoryId());

        Place place = placeService.findById(request.getPlaceId());

        User guide = null;
        if (request.getGuideId() != null) {
            guide = userService.findById(request.getGuideId());
        }

        Activity activity = activityMapper.toEntity(request);
        activity.setCategory(category);
        activity.setPlace(place);
        activity.setGuide(guide);

        Activity savedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(savedActivity);
    }

    public List<ActivityResponse> getAllActivities() {
        return activityRepository.findAll()
                .stream()
                .map(activityMapper::toResponse)
                .toList();
    }

    public ActivityResponse getActivityById(UUID id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with ID: " + id));
        return activityMapper.toResponse(activity);
    }

    public ActivityResponse updateActivity(UUID id,  ActivityRequest request) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with ID: " + id));

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

        Activity updatedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(updatedActivity);
    }

    public void deleteActivity(UUID id) {
        if (!activityRepository.existsById(id)) {
            throw new RuntimeException("Activity not found with ID: " + id);
        }
        activityRepository.deleteById(id);
    }
}
