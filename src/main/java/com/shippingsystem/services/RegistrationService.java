package com.shippingsystem.services;

import com.shippingsystem.models.User;
import com.shippingsystem.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

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

    public void createNewAccount(String email, String password, String username){
        User user = new User(email, username, password);
        user.setEnable(false);
        userRepository.save(user);
    }

    public void createNewVerifyToken(String email, String password, String username) {
        User user = userRepository.findByEmail(email);
        user.setPassword(password);
        user.setUsername(username);
    }
}
