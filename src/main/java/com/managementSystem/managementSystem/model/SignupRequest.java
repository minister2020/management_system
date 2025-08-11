package com.managementSystem.managementSystem.model;

import com.managementSystem.managementSystem.model.Enum.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    private String confirmPassword;

    private String email;
    @NotBlank(message = "Phone is required")

    private String phone;
    private Gender gender;
    private String maritalStatus;
    private String level;
}
