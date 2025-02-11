package org.ibtissam.dadesadventures.DTO.AuthDTO;

import jakarta.validation.constraints.*;
import lombok.*;
import org.ibtissam.dadesadventures.domain.enums.Role;
import org.ibtissam.dadesadventures.validation.EnumValue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {


    @NotBlank(message = "first name cannot be blank")
    private String firstName;

    @NotBlank(message = "last name cannot be blank")
    private String lastName;

    @NotBlank(message = "phone number cannot be blank")
    private String phoneNumber;

    @Email(message = "invalid email format")
    @NotBlank(message = "email cannot be blank")
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, message = "password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "password must contain at least one uppercase letter, one lowercase letter, and one number"
    )
    private String password;


    @NotNull(message = "role cannot be null")
    @EnumValue(enumClass = Role.class, message = "invalid role")
    private String role;

    private boolean isActive;
}
