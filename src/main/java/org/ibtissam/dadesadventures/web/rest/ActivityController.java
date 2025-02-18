package org.ibtissam.dadesadventures.web.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityRequest;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityResponse;
import org.ibtissam.dadesadventures.service.ActivityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/activities")
@AllArgsConstructor
public class ActivityController {
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(@Valid @RequestBody ActivityRequest request) {
        ActivityResponse response = activityService.createActivity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ActivityResponse>> getAllActivities(Pageable pageable) {
        Page<ActivityResponse> responses = activityService.getAllActivities(pageable);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteActivity(@PathVariable UUID id) {
        activityService.deleteActivity(id);
        return ResponseEntity.ok("Activity deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponse> updateActivity(@PathVariable UUID id,
                                                           @Valid @RequestBody ActivityRequest request) {
        ActivityResponse response = activityService.updateActivity(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
