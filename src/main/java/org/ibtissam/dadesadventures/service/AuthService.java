package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.Auth.AuthenticationRequest;
import org.ibtissam.dadesadventures.DTO.Auth.AuthenticationResponse;
import org.ibtissam.dadesadventures.DTO.Auth.RegisterRequest;
import org.ibtissam.dadesadventures.domain.entities.User;

public interface AuthService {
    User register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
