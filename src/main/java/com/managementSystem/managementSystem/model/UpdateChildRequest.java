package com.managementSystem.managementSystem.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateChildRequest {
    private String childName;
    private String school;
    private String age;
    private String schoolClass;

}

