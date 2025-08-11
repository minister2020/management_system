package com.managementSystem.managementSystem.controller;

import com.managementSystem.managementSystem.model.LoginRequest;
import com.managementSystem.managementSystem.model.LoginResponse;
import com.managementSystem.managementSystem.model.SignupRequest;
import com.managementSystem.managementSystem.model.User;
import com.managementSystem.managementSystem.repository.UserRepository;
import com.managementSystem.managementSystem.servive.UserService;
import com.managementSystem.managementSystem.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public String register(@RequestBody SignupRequest request) {
      return  userService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()
                )
        );

        String token = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());

        return ResponseEntity.ok(new LoginResponse("Login successful", token));
    }
}

