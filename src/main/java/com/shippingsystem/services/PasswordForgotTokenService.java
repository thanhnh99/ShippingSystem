package com.shippingsystem.services;

import com.shippingsystem.models.PasswordResetToken;
import com.shippingsystem.models.User;
import com.shippingsystem.repository.IPasswordResetTokenRepository;
import com.shippingsystem.repository.IUserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PasswordForgotTokenService {

    private final IUserRepository userRepository;
    private final IPasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordForgotTokenService(IUserRepository userRepository, IPasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public PasswordResetToken createPasswordToken(String email){
        User forgotPasswordUser = userRepository.findByEmail(email);
        if(forgotPasswordUser == null){
            return null;
        }
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUserId(forgotPasswordUser.getId());
        passwordResetTokenRepository.save(passwordResetToken);
        return passwordResetToken;
    }
    public User findUserByPasswordResetToken(String token){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken != null) {
            Optional<User> optionalUser = userRepository.findById(passwordResetToken.getUserId());
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            }
        }
        return null;
    }
}
