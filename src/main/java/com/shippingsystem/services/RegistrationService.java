package com.shippingsystem.services;

import com.shippingsystem.models.entity.Role;
import com.shippingsystem.models.entity.User;
import com.shippingsystem.repository.IRoleRepository;
import com.shippingsystem.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RegistrationService {
    @Autowired
    IRoleRepository roleRepository;

    private final IUserRepository userRepository;

    public RegistrationService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkForExistingAccount(String email){
        User user = userRepository.findByEmail(email);
        if(user != null && user.isEnable()) {
            return true;
        }
        else return false;
    }

    public void createNewAccount(String email, String password, String username, String adress){
        Role role = roleRepository.findByName("USER");
        User user = new User(email, username, password, adress,role);
        user.setEnable(false);
        userRepository.save(user);
    }

    public void createNewVerifyToken(String email, String password, String username) {
        User user = userRepository.findByEmail(email);
        user.setPassword(password);
        user.setUsername(username);
    }
}
