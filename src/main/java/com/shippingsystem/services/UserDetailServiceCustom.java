package com.shippingsystem.services;

import com.shippingsystem.models.User;
import com.shippingsystem.models.UserDetailCustom;
import com.shippingsystem.repository.IUserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceCustom implements UserDetailsService {

    private final IUserRepository userRepository;

    public UserDetailServiceCustom(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailCustom loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);
        if(user == null) {
            return null;
        }
        return new UserDetailCustom(user);
    }

    public UserDetailCustom loadUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        if(user == null) {
            return null;
        }
        return new UserDetailCustom(user);
    }

}
