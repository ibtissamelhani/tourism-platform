package org.ibtissam.dadesadventures.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Auth.AuthenticationRequest;
import org.ibtissam.dadesadventures.DTO.Auth.AuthenticationResponse;
import org.ibtissam.dadesadventures.DTO.Auth.RegisterRequest;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.ibtissam.dadesadventures.domain.enums.Role;
import org.ibtissam.dadesadventures.exception.user.EmailAlreadyExistException;
import org.ibtissam.dadesadventures.exception.user.InvalidCredentialsException;
import org.ibtissam.dadesadventures.exception.user.UserNotFoundException;
import org.ibtissam.dadesadventures.repository.UserRepository;
import org.ibtissam.dadesadventures.security.JwtService;
import org.ibtissam.dadesadventures.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthService {

    private final UserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public User register(RegisterRequest request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailAlreadyExistException("Email already exist");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.TOURIST)
                .isActive(true)
                .build();

       return appUserRepository.save(user);

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new UserNotFoundException("User not found"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
       var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
