package org.ibtissam.dadesadventures.web.rest;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Auth.AuthenticationRequest;
import org.ibtissam.dadesadventures.DTO.Auth.AuthenticationResponse;
import org.ibtissam.dadesadventures.DTO.Auth.RegisterRequest;
import org.ibtissam.dadesadventures.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully."));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
