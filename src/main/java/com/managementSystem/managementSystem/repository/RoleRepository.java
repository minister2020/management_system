package com.managementSystem.managementSystem.repository;

import com.managementSystem.managementSystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

//    Optional<Role> findByName(String name);

    Optional<Role> findByRoleName(String roleName);

    }

