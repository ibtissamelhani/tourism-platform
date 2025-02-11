package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.AuthDTO.AuthenticationRequest;
import org.ibtissam.dadesadventures.DTO.AuthDTO.AuthenticationResponse;
import org.ibtissam.dadesadventures.DTO.AuthDTO.RegisterRequest;
import org.ibtissam.dadesadventures.domain.entities.User;

public interface AuthService {
    User register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
