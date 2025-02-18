package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.Activity.ActivityRequest;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ActivityService {

    ActivityResponse createActivity(ActivityRequest request);
    Page<ActivityResponse> getAllActivities(Pageable pageable);
    public void deleteActivity(UUID id);
    ActivityResponse updateActivity(UUID id, ActivityRequest request);
}
