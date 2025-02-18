package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.Activity.ActivityRequest;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityResponse;
import org.ibtissam.dadesadventures.DTO.Activity.ActivitySearchDTO;
import org.ibtissam.dadesadventures.domain.entities.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ActivityService {

    ActivityResponse createActivity(ActivityRequest request);
    Page<ActivityResponse> getAllActivities(Pageable pageable);
    void deleteActivity(UUID id);
    ActivityResponse updateActivity(UUID id, ActivityRequest request);
    Page<ActivityResponse> searchActivities(ActivitySearchDTO searchDTO, Pageable pageable);
    Activity findById(UUID id);
}
