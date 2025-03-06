package org.ibtissam.dadesadventures.web.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Place.PlaceRequest;
import org.ibtissam.dadesadventures.DTO.Place.PlaceResponse;
import org.ibtissam.dadesadventures.service.PlaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/places")
@AllArgsConstructor
public class PlaceController {
    private PlaceService placeService;

    @PostMapping
    public ResponseEntity<PlaceResponse> createPlace(@RequestBody @Valid PlaceRequest placeRequest) {
        PlaceResponse response = placeService.createPlace(placeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PlaceResponse>> getAllPlaces(Pageable pageable) {
        Page<PlaceResponse> places = placeService.getAllPlaces(pageable);
        return ResponseEntity.ok(places);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponse> getPlaceById(@PathVariable UUID id) {
        PlaceResponse response = placeService.getPlaceById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceResponse> updatePlace(@PathVariable UUID id, @RequestBody @Valid PlaceRequest placeRequest) {
        PlaceResponse response = placeService.updatePlace(id, placeRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlace(@PathVariable UUID id) {
        placeService.deletePlace(id);
        return ResponseEntity.ok("Place deleted successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlaceResponse>> searchPlaces(@RequestParam("name") String name) {
        List<PlaceResponse> places = placeService.searchPlacesByName(name);
        return ResponseEntity.ok(places);
    }
}
