package com.hcmus.dto.request;


import com.hcmus.dto.response.ErrorCodes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Email(message = ErrorCodes.WRONG_EMAIL_FORMAT, regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = ErrorCodes.EMPTY)
    private String email;
    private String password;
    @NotEmpty(message = ErrorCodes.EMPTY)
    private String firstName;
    private String lastName;
}
