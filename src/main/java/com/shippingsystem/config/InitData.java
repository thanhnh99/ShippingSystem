package com.shippingsystem.config;

import com.shippingsystem.models.entity.Item;
import com.shippingsystem.models.entity.Role;
import com.shippingsystem.models.entity.User;
import com.shippingsystem.repository.IItemRepository;
import com.shippingsystem.repository.IRoleRepository;
import com.shippingsystem.repository.IUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class InitData {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final IItemRepository itemRepository;

    public InitData(IRoleRepository roleRepository, IUserRepository userRepository, IItemRepository itemRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Bean
    public void addRole(){
        if(roleRepository.getFirstByRoleName("USER") == null) {
            roleRepository.save(new Role("USER", new ArrayList<>()));
        }
        if(roleRepository.getFirstByRoleName("ADMIN") == null) {
            roleRepository.save(new Role("ADMIN", new ArrayList<>()));
        }
        if(roleRepository.getFirstByRoleName("SHIPPER") == null) {
            roleRepository.save(new Role("SHIPPER", new ArrayList<>()));
        }
    }

    @Bean
    public void AddAdmin()
    {
        if(userRepository.findByEmail("admin@gmail.com") == null) {
            Role role = roleRepository.getFirstByRoleName("ADMIN");
            User admin = new User("admin@gmail.com", "ADMIN", "1234", "HANOI",role, "012345678");
            admin.setEnable(true);
            role.getUsers().add(admin);
            userRepository.save(admin);
            roleRepository.save(role);
        }
    }

    @Bean
    public void AddItem()
    {
        if(itemRepository.findByCode("big-item") == null) {
            itemRepository.save(new Item("BIG ITEM","big-item",3,new ArrayList<>()));
        }
        if(itemRepository.findByCode("small-item") == null) {
            itemRepository.save(new Item("SMALL ITEM","small-item",1,new ArrayList<>()));
        }
        if(itemRepository.findByCode("normal-item") == null) {
            itemRepository.save(new Item("NORMAL ITEM","normal-item",2,new ArrayList<>()));
        }
    }
}
