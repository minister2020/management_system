package com.managementSystem.managementSystem.servive;

import com.managementSystem.managementSystem.constants.Constants;
import com.managementSystem.managementSystem.model.Enum.Gender;
import com.managementSystem.managementSystem.model.Role;
import com.managementSystem.managementSystem.model.SignupRequest;
import com.managementSystem.managementSystem.model.User;
import com.managementSystem.managementSystem.repository.RoleRepository;
import com.managementSystem.managementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private RoleService roleService;


    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
@Transactional
    public String registerUser(SignupRequest request) {
        String phone = request.getPhone();
            try {
                
                // Step 1: Check if email or phone already exists
                if (userRepository.existsByEmail(request.getEmail())) {
                    return "Email already exists";
                }

                if (userRepository.existsByPhone(request.getPhone())) {
                    return "Phone already exists";
                }

                if (!request.getPassword().equals(request.getConfirmPassword())) {
                    return "Passwords do not match.";
                }
                if (!isValidNigerianPhoneNumber(phone)) {
                    return Constants.INVALID_PHONE_NUMBER + " " + phone;
                }
                if (userRepository.existsByUsername(request.getUsername())) {
                    return "Username already taken";
                }
                Role role = roleRepository.findByRoleName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Error: Default role not found."));

                if ("ROLE_ADMIN".equals(role.getRoleName())) {
                    throw new RuntimeException("You cannot assign yourself an admin role");
                }

                // Step 2: Create user object
                User newUser = new User();
                newUser.setUsername(request.getUsername());
                newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                newUser.setEmail(request.getEmail());
                newUser.setPhone(request.getPhone());
                try {
                    newUser.setGender(Gender.valueOf(String.valueOf(request.getGender())));
                } catch (IllegalArgumentException | NullPointerException e) {
                    throw new IllegalArgumentException("Invalid gender provided. Must be MALE or FEMALE.");
                }

                newUser.setMaritalStatus(request.getMaritalStatus());
                newUser.setLevel(request.getLevel());
                Role defaultRole = roleRepository.findByRoleName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Default role not found"));

                // Assign role
                newUser.getRoles().add(defaultRole);
               User savedUser = userRepository.save(newUser);

                // Step 6: Send welcome message (if valid email provided)
                if (isValidEmail(savedUser.getEmail())) {
                    sendWelcomeEmail(savedUser.getEmail(), savedUser.getUsername());
                }

                return "User registered successfully";

            } catch (DataIntegrityViolationException ex) {
                return "Email or phone already exists (constraint error)";
            } catch (Exception e) {
                e.printStackTrace();
                return "An error occurred: " + e.getMessage();
            }
        }

    private void sendWelcomeEmail(String email, String username) {
        String subject = "Welcome to Our Isolo-Ejigbo Shubah Management Platform!";
        String message = "Hello " + username + ",\n\n" +
                "Thank you for registering with us. We’re excited to have you onboard.\n\n" +
                "Best regards,\n" +
                "Mudir";
        mailService.sendEmail(email, subject, message);
    }



    private boolean isValidNigerianPhoneNumber(String phone) {
        return phone.matches("^0[789][01]\\d{8}$");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    @Transactional
    public void assignRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

}
