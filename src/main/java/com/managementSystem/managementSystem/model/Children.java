package com.managementSystem.managementSystem.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Children {
    private String childName;
    private String school;
    private String age;
    private String schoolClass;
}
