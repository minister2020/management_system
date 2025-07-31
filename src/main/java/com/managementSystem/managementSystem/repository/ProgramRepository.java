package com.managementSystem.managementSystem.repository;

import com.managementSystem.managementSystem.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    List<Program> findByName(String name);
}
