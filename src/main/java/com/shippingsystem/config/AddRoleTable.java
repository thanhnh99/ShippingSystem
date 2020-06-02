package com.shippingsystem.config;

import com.shippingsystem.models.entity.Role;
import com.shippingsystem.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class AddRoleTable {
    private final IRoleRepository roleRepository;

    public AddRoleTable(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    public void addRole(){
        roleRepository.save(new Role("USER", new ArrayList<>()));
        roleRepository.save(new Role("ADMIN", new ArrayList<>()));
    }
}
