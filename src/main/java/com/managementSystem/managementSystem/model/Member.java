package com.managementSystem.managementSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String workInfo;
    private String address;


    @OneToMany(cascade = CascadeType.ALL)
    private List<Member> kids;

    @OneToOne(cascade = CascadeType.ALL)
    private Member spouse;

    private String family;

}
