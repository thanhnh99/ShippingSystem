package com.shippingsystem.models.request;

import lombok.Data;

@Data
public class NewPasswordRequest {
    private String token;
    private String newPassword;
    private String newPasswordConfirm;
}
