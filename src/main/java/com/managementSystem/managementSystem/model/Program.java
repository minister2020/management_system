package com.managementSystem.managementSystem.model;

import com.managementSystem.managementSystem.model.Enum.ProgramMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String venue;

    private String description;

    private LocalDate programDate;

    private LocalTime programTime;

    @Enumerated(EnumType.STRING)
    private ProgramMode programIsFor;

}
