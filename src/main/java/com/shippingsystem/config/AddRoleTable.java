package com.shippingsystem.config;

import com.shippingsystem.models.entity.Role;
import com.shippingsystem.models.entity.User;
import com.shippingsystem.repository.IRoleRepository;
import com.shippingsystem.repository.IUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AddRoleTable {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;

    public AddRoleTable(IRoleRepository roleRepository, IUserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public void addRole(){
        if(roleRepository.getFirstByRoleName("USER") == null) {
            roleRepository.save(new Role("USER", new ArrayList<>()));
        }
        if(roleRepository.getFirstByRoleName("ADMIN") == null) {
            roleRepository.save(new Role("ADMIN", new ArrayList<>()));
        }

        if(userRepository.findByEmail("admin@gmail.com") == null) {
            Role role = roleRepository.getFirstByRoleName("ADMIN");
            User admin = new User("admin@gmail.com", "ADMIN", "1234", "HANOI",role);
            role.getUsers().add(admin);
            userRepository.save(admin);
            roleRepository.save(role);
        }

    }
}
