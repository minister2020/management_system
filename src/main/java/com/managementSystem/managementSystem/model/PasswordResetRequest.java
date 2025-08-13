package com.managementSystem.managementSystem.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordResetRequest {
    private String username;
    private String newPassword;

}

