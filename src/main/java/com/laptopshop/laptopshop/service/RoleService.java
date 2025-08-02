package com.laptopshop.laptopshop.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.laptopshop.laptopshop.domain.Role;
import com.laptopshop.laptopshop.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> fetchRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }
}
