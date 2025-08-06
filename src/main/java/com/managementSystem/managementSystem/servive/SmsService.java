package com.managementSystem.managementSystem.servive;

import com.managementSystem.managementSystem.model.Program;
import com.managementSystem.managementSystem.util.ProgramUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    @Value("${termii.apiKey}")
    private String termiiApiKey;
    @Autowired
    private ProgramUtil programUtil;

    private final String termiiUrl = "https://api.ng.termii.com/api/sms/send";

    public void sendSms(String phoneNumber, Program program) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("to", phoneNumber);
        body.put("from", "Mudir");
        body.put("sms", programUtil.buildProgramNotification(program));
        body.put("type", "plain");
        body.put("channel", "generic");
        body.put("api_key", termiiApiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(termiiUrl, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to send SMS: " + response.getBody());
        }

        System.out.println("✅ SMS sent to " + phoneNumber);
    }

    public void sendSmsUpdate(String phoneNumber, Program program) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        String smsContent = programUtil.buildProgramUpdateNotification(program);
        System.out.println("📩 SMS Content: " + smsContent);
        body.put("to", phoneNumber);
        body.put("from", "Mudir");
        body.put("sms", smsContent);
        body.put("type", "plain");
        body.put("channel", "generic");
        body.put("api_key", termiiApiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(termiiUrl, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to send SMS: " + response.getBody());
        }

        System.out.println("✅ SMS sent to " + phoneNumber);
    }
}

