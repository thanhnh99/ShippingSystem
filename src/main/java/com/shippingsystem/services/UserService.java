package com.shippingsystem.services;

import com.shippingsystem.models.auth.PasswordResetToken;
import com.shippingsystem.models.auth.ResponseStatus;
import com.shippingsystem.models.auth.UserDetailCustom;
import com.shippingsystem.models.auth.VerificationToken;
import com.shippingsystem.models.entity.User;
import com.shippingsystem.models.request.*;
import com.shippingsystem.models.response.*;
import com.shippingsystem.repository.IUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RegistrationService registrationService;
    private final VerificationTokenRegistrationService verificationTokenRegistrationService;
    private final SendingMailService sendingMailService;
    private final PasswordForgotTokenService passwordForgotTokenService;

    public UserService(AuthenticationManager authenticationManager,
                       IUserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider,
                       RegistrationService registrationService,
                       VerificationTokenRegistrationService verificationTokenRegistrationService,
                       SendingMailService sendingMailService,
                       PasswordForgotTokenService passwordForgotTokenService) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.registrationService = registrationService;
        this.verificationTokenRegistrationService = verificationTokenRegistrationService;
        this.sendingMailService = sendingMailService;
        this.passwordForgotTokenService = passwordForgotTokenService;
    }

    public LoginResponse authenticateUser(LoginRequest loginRequest){

        LoginResponse response = new LoginResponse();
        final UserDetailCustom userDetails = loadUserByEmail(loginRequest.getEmail());

        if(userDetails == null
                || !loginRequest.getPassword().equals(userDetails.getPassword())){

            response.setStatus(ResponseStatus.HAVE_NOT_ACCOUNT);

        } else {
            if(userDetails.getUser().isEnable()){
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                ));

                final String token = jwtTokenProvider.generateToken(userDetails);
                response.setStatus(ResponseStatus.ACCEPT);
                response.setToken(token);
                response.setRole(userDetails.getAuthorities());
            } else
                response.setStatus(ResponseStatus.ACCOUNT_IS_INACTIVE);
        }

    return response;
    }

    public RegistrationResponse registerNewAccount(RegistrationRequest request){
        RegistrationResponse response = new RegistrationResponse();

        if(registrationService.checkForExistingAccount(request.getEmail())){
            response.setStatus(ResponseStatus.EMAIL_ALREADY_EXISTS);
        } else {
            User user = userRepository.findByEmail(request.getEmail());
            if(user != null  ) {
                registrationService.createNewVerifyToken(request.getEmail(),
                        request.getPassword(),
                        request.getUsername());
            }  else {
                registrationService.createNewAccount(request.getEmail(),
                        request.getUsername(),
                        request.getPassword(),
                        request.getAddress(),
                        request.getPhone());
            }

            if(!sendingTokenToVerifyEmail(request.getEmail())) {
                response.setStatus(ResponseStatus.INVALID_EMAIL);
            } else {
                response.setStatus(ResponseStatus.SENT_EMAIL);
            }
        }
        return response;
    }
    public boolean sendingTokenToVerifyEmail(String email){
        VerificationToken verificationToken = null;
        verificationToken = verificationTokenRegistrationService.createVerification(email);
        if(verificationToken == null){
            return false;
        }
        sendingMailService.sendVerificationMail(email,verificationToken.getToken());
        return true;
    }

    public PasswordResetResponse sendingResetPasswordEmail(PasswordResetRequest request){

        PasswordResetResponse response = new PasswordResetResponse();
        response.setEmail(request.getEmail());

        PasswordResetToken passwordResetToken = passwordForgotTokenService.createPasswordToken(request.getEmail());
        if(passwordResetToken == null){
            response.setStatus(ResponseStatus.HAVE_NOT_ACCOUNT);
        } else {
            if(sendingMailService.sendPasswordResetMail(request.getEmail(),passwordResetToken.getToken())){
                response.setStatus(ResponseStatus.SENT_EMAIL);
            }
            else response.setStatus(ResponseStatus.ERROR);
        }

        return response;
    }


    public VerificationResetPasswordTokenResponse verifyResetPasswordToken(String token){
        VerificationResetPasswordTokenResponse response = new VerificationResetPasswordTokenResponse();
        User user = passwordForgotTokenService.findUserByPasswordResetToken(token);
        if(user == null){
            response.setStatus(ResponseStatus.HAVE_NOT_ACCOUNT);
        } else  if (user.isEnable() == false) {
            response.setStatus(ResponseStatus.ACCOUNT_IS_INACTIVE);
        } else {
            response.setEmail(user.getEmail());
            response.setStatus(ResponseStatus.SUCCESSFULLY_VERIFY);
        }

        return response;
    }

    public NewPasswordResponse setNewPassword(NewPasswordRequest request){
        NewPasswordResponse response = new NewPasswordResponse();
        User user = passwordForgotTokenService.findUserByPasswordResetToken(request.getToken());
        if(user == null) response.setStatus(ResponseStatus.HAVE_NOT_ACCOUNT);
        else {
            if(request.getNewPassword().equals(request.getNewPasswordConfirm()) == false){
                response.setStatus(ResponseStatus.INVALID_CONFIRM_PASSWORD);
            } else {
                saveNewPassword(user.getEmail(), request.getNewPassword());
                response.setEmail(user.getEmail());
                response.setStatus(ResponseStatus.SAVED_NEW_PASSWORD);
            }
        }

        return response;
    }

    public void saveNewPassword(String email, String newPassword){
        User user = findUserByEmail(email);
        user.setPassword(newPassword);
        saveUser(user);
    }


    public String getEmailFromToken(String token){
        String email = null;

        try {
            email = jwtTokenProvider.getEmailFromToken(token);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return email;
    }

    public boolean validateToken(String email, String token){
        try {
            if(jwtTokenProvider.validateToken(token, email)){
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            return false;
        }

        return false;
    }

    public UserDetailCustom loadUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        if(user == null) {
            return null;
        }
        return new UserDetailCustom(user);
    }

    public User findUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        if(user == null) {
            return null;
        }
        return user;
    }
    public void saveUser(User user){
        userRepository.save(user);
    }
}
