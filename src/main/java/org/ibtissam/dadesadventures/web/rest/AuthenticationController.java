package org.ibtissam.dadesadventures.web.rest;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.AuthDTO.AuthenticationRequest;
import org.ibtissam.dadesadventures.DTO.AuthDTO.AuthenticationResponse;
import org.ibtissam.dadesadventures.DTO.AuthDTO.RegisterRequest;
import org.ibtissam.dadesadventures.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        authenticationService.register(request);
        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
