package org.ibtissam.dadesadventures.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.user.UpdateUser;
import org.ibtissam.dadesadventures.DTO.user.UserDTOMapper;
import org.ibtissam.dadesadventures.DTO.user.UserRequest;
import org.ibtissam.dadesadventures.DTO.user.UserResponse;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.ibtissam.dadesadventures.domain.enums.Role;
import org.ibtissam.dadesadventures.exception.user.EmailAlreadyExistException;
import org.ibtissam.dadesadventures.exception.user.UserNotFoundException;
import org.ibtissam.dadesadventures.repository.UserRepository;
import org.ibtissam.dadesadventures.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if(userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            throw new EmailAlreadyExistException("Email already exist");
        }
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()))
                .phoneNumber(userRequest.getPhoneNumber())
                .role(Role.valueOf(userRequest.getRole()))
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);
        return (userDTOMapper.toUserDTO(savedUser));
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userDTOMapper::toUserDTO);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return userDTOMapper.toUserDTO(user);
    }
    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return userDTOMapper.toUserDTO(user);
    }

    @Override
    public UserResponse updateUser(UUID id, UpdateUser userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        user.setFirstName(userRequest.getFirstName() != null ? userRequest.getFirstName() : user.getFirstName());
        user.setLastName(userRequest.getLastName() != null ? userRequest.getLastName() : user.getLastName());
        user.setEmail(userRequest.getEmail() != null ? userRequest.getEmail() : user.getEmail());
        //user.setPassword(userRequest.getPassword() != null ? BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()) : user.getPassword()); // Password should be encoded in a real application
        user.setPhoneNumber(userRequest.getPhoneNumber() != null ? userRequest.getPhoneNumber() : user.getPhoneNumber());
        user.setRole(userRequest.getRole() != null ? Role.valueOf(userRequest.getRole()) : user.getRole());
        user.setActive(userRequest.getIsActive() != user.isActive() ? userRequest.getIsActive() : user.isActive());
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return (userDTOMapper.toUserDTO(updatedUser));
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponse> getUsersByRole(Role role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream()
                .map(userDTOMapper::toUserDTO)
                .toList();
    }


}
