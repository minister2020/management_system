package com.managementSystem.managementSystem.model;

import jakarta.persistence.*;
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

    @ElementCollection
    @CollectionTable(
            name = "member_children",
            joinColumns = @JoinColumn(name = "member_id")
    )
    private List<Children> children;

    private String spouse;

    private String family;

}
