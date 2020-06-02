package com.shippingsystem.controller;

import com.shippingsystem.models.auth.ResponseStatus;
import com.shippingsystem.services.VerificationTokenRegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerifyController {
    private final VerificationTokenRegistrationService verificationTokenRegistrationService;

    public VerifyController(VerificationTokenRegistrationService verificationTokenRegistrationService) {
        this.verificationTokenRegistrationService = verificationTokenRegistrationService;
    }

    @GetMapping("/verifying-email")
    public String verifyEmail(@RequestParam("token") String token) {
        String response = verificationTokenRegistrationService.verifyEmail(token);
        if(response.equals(ResponseStatus.SUCCESSFULLY_VERIFY)){
            return "verify_email.html";
        }
        return "verify-fail.html";
    }
}
