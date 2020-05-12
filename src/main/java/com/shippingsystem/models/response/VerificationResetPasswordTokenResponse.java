package com.shippingsystem.models.response;

import lombok.Data;

@Data
public class VerificationResetPasswordTokenResponse {
    private String status;
    private String email;
}
