package com.managementSystem.managementSystem.controller;

import com.managementSystem.managementSystem.servive.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestParam String username,
                                             @RequestParam String roleName) {
        userService.assignRoleToUser(username, roleName);
        return ResponseEntity.ok("Role '" + roleName + "' assigned to user: " + username);
    }
}

