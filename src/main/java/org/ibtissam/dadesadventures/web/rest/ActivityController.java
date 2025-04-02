package org.ibtissam.dadesadventures.web.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityRequest;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityResponse;
import org.ibtissam.dadesadventures.DTO.Activity.ActivitySearchDTO;
import org.ibtissam.dadesadventures.service.ActivityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
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

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable UUID id) {
        ActivityResponse response = activityService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('MANAGE_ACTIVITIES')")
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

    @PostMapping("/search")
    public ResponseEntity<Page<ActivityResponse>> searchActivities(
            @RequestBody ActivitySearchDTO searchDTO,
            Pageable pageable) {

        return ResponseEntity.ok(activityService.searchActivities(searchDTO, pageable));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelActivity(@PathVariable UUID id) {
        activityService.cancelActivity(id);
        return ResponseEntity.ok("Activity cancelled successfully.");
    }
}
