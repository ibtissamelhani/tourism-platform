package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.AuthDTO.AuthenticationRequest;
import org.ibtissam.dadesadventures.DTO.AuthDTO.AuthenticationResponse;
import org.ibtissam.dadesadventures.DTO.AuthDTO.RegisterRequest;

public interface AuthService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
