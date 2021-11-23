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
    @NotBlank(message = "{msg.error.username.required}")
    @Size(min = 3, max = 20, message = "{msg.error.username.limits}")
    private String username;

    @NotBlank(message = "{msg.error.password.required}")
    @Size(min = 6, message = "{msg.error.password.limits}")
    private String password;
    private String matchingPassword;
}
