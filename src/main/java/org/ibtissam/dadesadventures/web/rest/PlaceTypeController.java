package org.ibtissam.dadesadventures.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.domain.entities.PlaceType;
import org.ibtissam.dadesadventures.service.PlaceTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/types")
@RequiredArgsConstructor
public class PlaceTypeController {
    private final PlaceTypeService typeService;

    @GetMapping
    public ResponseEntity<List<PlaceType>> getAllTypes() {
        return ResponseEntity.ok(typeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceType> getTypeById(@PathVariable UUID id) {
        return ResponseEntity.ok(typeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PlaceType> createType(@RequestBody @Valid PlaceType type) {
        return ResponseEntity.status(HttpStatus.CREATED).body(typeService.create(type));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceType> updateType(@PathVariable UUID id, @RequestBody @Valid PlaceType type) {
        return ResponseEntity.ok(typeService.update(id, type));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteType(@PathVariable UUID id) {
        typeService.delete(id);
        return ResponseEntity.ok("Type deleted successfully.");
    }
}
