package com.shippingsystem.services;

import com.shippingsystem.models.ResponseStatus;
import com.shippingsystem.models.User;
import com.shippingsystem.models.VerificationToken;
import com.shippingsystem.repository.IUserRepository;
import com.shippingsystem.repository.IVerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class VerificationTokenRegistrationService {

    private final IUserRepository userRepository;
    private final IVerificationTokenRepository verificationTokenRepository;

    public VerificationTokenRegistrationService(IUserRepository userRepository, IVerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public VerificationToken createVerification(String email){
        User user = userRepository.findByEmail(email);

        if(!user.isEnable()){
            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setUserId(user.getId());
            verificationTokenRepository.save(verificationToken);
            return verificationToken;
        }
        return null;
    }

    public String verifyEmail(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null) {
            return ResponseStatus.INVALID_TOKEN;
        }

        if(verificationToken.getExpiryDateTime().isBefore(LocalDateTime.now())){
            return ResponseStatus.EXPIRED_TOKEN;
        }


        verificationToken.setConfirmDateTime(LocalDateTime.now());
        verificationToken.setStatus(VerificationToken.STATUS_VERIFIED);
        verificationTokenRepository.save(verificationToken);
        Optional<User> optionalUser = userRepository.findById(verificationToken.getUserId());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnable(true);
            userRepository.save(user);
            return ResponseStatus.SUCCESSFULLY_VERIFY;
        }
        return ResponseStatus.INVALID_TOKEN;
    }

    public User findUserByVerificationToken(String token){
        VerificationToken verificationToken = null;
        try {
            verificationToken = verificationTokenRepository.findByToken(token);
            Optional<User> optionalUser = userRepository.findById(verificationToken.getUserId());
            if(optionalUser.isPresent()) {
                return optionalUser.get();
            } else return null;
        } catch (Exception exception) {
            return null;
        }
    }
}
