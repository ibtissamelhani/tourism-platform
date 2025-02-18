package org.ibtissam.dadesadventures.service;


import org.ibtissam.dadesadventures.DTO.user.UpdateUser;
import org.ibtissam.dadesadventures.DTO.user.UserRequest;
import org.ibtissam.dadesadventures.DTO.user.UserResponse;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUserById(UUID id);
    UserResponse getUserByEmail(String email);
    UserResponse updateUser(UUID id, UpdateUser userRequest);
    void deleteUser(UUID id);
    User findById(UUID id);
}
