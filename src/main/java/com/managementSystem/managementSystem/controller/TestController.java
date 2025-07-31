package com.managementSystem.managementSystem.controller;

import com.managementSystem.managementSystem.servive.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private MailService mailService;

    @GetMapping("/api/test")
    public String test(){
        return "API is working";
    }

    // ... existing code ...
    @GetMapping("/api/test-mail")
    public String testMail() {

        mailService.sendEmail("abdulraheedidowu86@gmail.com", "Test", "This is a test email.");
        return "Mail sent!";
    }
}
