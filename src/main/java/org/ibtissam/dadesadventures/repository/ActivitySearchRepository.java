package org.ibtissam.dadesadventures.repository;

import org.ibtissam.dadesadventures.DTO.Activity.ActivitySearchDTO;
import org.ibtissam.dadesadventures.domain.entities.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivitySearchRepository{
   List<Activity> findByCriteria(ActivitySearchDTO activitySearchDTO);
}
