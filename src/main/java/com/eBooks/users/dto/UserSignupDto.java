package com.eBooks.users.dto;

import com.eBooks.users.dto.validator.PasswordMatches;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatches
public class UserSignupDto {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Usernmae is a min of 3 and max of 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password needs to be at least 6 characters")
    private String password;
    private String matchingPassword;
}
