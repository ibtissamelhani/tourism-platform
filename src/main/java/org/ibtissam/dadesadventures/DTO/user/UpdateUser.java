package org.ibtissam.dadesadventures.DTO.user;

import jakarta.validation.constraints.*;
import lombok.*;
import org.ibtissam.dadesadventures.domain.enums.Role;
import org.ibtissam.dadesadventures.validation.EnumValue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUser {
    private String firstName;

    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

//    @Size(min = 8, message = "password must be at least 8 characters")
//    @Pattern(
//            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
//            message = "password must contain at least one uppercase letter, one lowercase letter, and one number"
//    )private String password;

    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "phone number must be between 10 and 15 digits and can start with a '+'"
    )
    private String phoneNumber;

    @EnumValue(enumClass = Role.class, message = "invalid role")
    private String role;

    private Boolean isActive;
}
