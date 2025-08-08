package com.managementSystem.managementSystem.util;

import org.springframework.stereotype.Component;

@Component
public class MemberUtil {

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    public boolean isValidNigerianPhoneNumber(String phoneNumber) {
        String regex = "^0(70[1-9]|80[2-9]|81[0-9]|90[1-9]|91[0-9]|91[0-9]|701|703|705|706|707|708|709|802|803|804|805|806|807|808|809|810|811|812|813|814|815|816|817|818|819|909|912|913|914|915|916|917|918|919)[0-9]{6}$";
        return phoneNumber != null && phoneNumber.matches(regex);
    }
}
