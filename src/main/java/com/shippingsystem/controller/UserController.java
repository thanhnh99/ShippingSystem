package com.shippingsystem.controller;

import com.shippingsystem.models.auth.ResponseStatus;
import com.shippingsystem.models.request.LoginRequest;
import com.shippingsystem.models.request.NewPasswordRequest;
import com.shippingsystem.models.request.PasswordResetRequest;
import com.shippingsystem.models.request.RegistrationRequest;
import com.shippingsystem.models.response.*;
import com.shippingsystem.services.UserService;
import com.shippingsystem.services.VerificationTokenRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, VerificationTokenRegistrationService verificationTokenRegistrationService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.authenticateUser(loginRequest);

        if(response.getStatus().equals(ResponseStatus.HAVE_NOT_ACCOUNT)){
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity sendPasswordReset(@RequestBody @Valid PasswordResetRequest passwordForgotRequest){
        PasswordResetResponse response = userService.sendingResetPasswordEmail(passwordForgotRequest);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/verifying-reset-password")
    public ResponseEntity setNewPassword(@RequestParam("token") String token, @RequestBody NewPasswordRequest request){
        System.out.println(token);
        NewPasswordResponse response = userService.setNewPassword(request);
        if(response.getStatus() == ResponseStatus.SAVED_NEW_PASSWORD){
            return new ResponseEntity(response, HttpStatus.OK);
        } else return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegistrationRequest request){
        RegistrationResponse response = userService.registerNewAccount(request);
        if(response.getStatus() == ResponseStatus.EMAIL_ALREADY_EXISTS) {
            return new ResponseEntity(ResponseStatus.HAVE_EXIST_ACCOUNT,HttpStatus.BAD_REQUEST);
        } else if(response.getStatus() == ResponseStatus.INVALID_EMAIL){
            return new ResponseEntity(ResponseStatus.INVALID_EMAIL,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/verifying-reset-password")
    public VerificationResetPasswordTokenResponse verifyResetPasswordToken(@RequestParam("token") String token){
        return userService.verifyResetPasswordToken(token);
    }
}