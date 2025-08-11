package com.managementSystem.managementSystem.servive;

import com.managementSystem.managementSystem.model.Role;
import com.managementSystem.managementSystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

//    public Role getRoleByName(String roleName) {
//        return roleRepository.findByName(roleName)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//    }
}

