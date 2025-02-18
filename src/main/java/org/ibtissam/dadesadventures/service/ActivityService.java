package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.Activity.ActivityRequest;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityResponse;

public interface ActivityService {

    ActivityResponse createActivity(ActivityRequest request);
}
