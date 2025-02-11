package org.ibtissam.dadesadventures.DTO.user;

import jakarta.validation.constraints.*;
import lombok.*;
import org.ibtissam.dadesadventures.domain.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone number must be between 10 and 15 digits and can start with a '+'"
    )
    private String phoneNumber;

    @NotNull(message = "Role is required")
    private Role role;

    private boolean isActive;
}
