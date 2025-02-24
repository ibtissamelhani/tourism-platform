package org.ibtissam.dadesadventures.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityDTOMapper;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityRequest;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityResponse;
import org.ibtissam.dadesadventures.DTO.Activity.ActivitySearchDTO;
import org.ibtissam.dadesadventures.domain.entities.*;
import org.ibtissam.dadesadventures.exception.activity.ActivityNotFoundException;
import org.ibtissam.dadesadventures.exception.user.GuideIsBusyException;
import org.ibtissam.dadesadventures.repository.ActivityRepository;
import org.ibtissam.dadesadventures.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    @Override
    public ActivityResponse createActivity(ActivityRequest request) {
        Category category = categoryService.findById(request.getCategoryId());

        Place place = placeService.findById(request.getPlaceId());

        User guide = null;
        if (request.getGuideId() != null) {
            guide = userService.findById(request.getGuideId());
            boolean isBusy = activityRepository.isGuideBusy(request.getGuideId(), request.getDate());
            if (isBusy) {
                throw new GuideIsBusyException("guide is busy");
            }
        }
        Activity activity = Activity.builder()
                .name(request.getName())
                .date(request.getDate())
                .registrationDeadline(request.getRegistrationDeadline())
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
        activity.setRegistrationDeadline(request.getRegistrationDeadline());
        activity.setAvailability(request.getAvailability());
        activity.setCategory(category);
        activity.setPlace(place);
        activity.setGuide(guide);
        activity.setUpdatedAt(LocalDateTime.now());

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

        Activity updatedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(updatedActivity);
    }

    @Override
    public Page<ActivityResponse> searchActivities(ActivitySearchDTO searchDTO, Pageable pageable) {
        List<Activity> activities = activityRepository.findByCriteria(searchDTO);

        List<ActivityResponse> responses = activities.stream()
                .map(activityMapper::toResponse)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responses.size());

        List<ActivityResponse> paginatedResponses = responses.subList(start, end);

        return new PageImpl<>(paginatedResponses, pageable, responses.size());
    }

    @Override
    public Activity findById(UUID id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("activity not found"));
    }

}
