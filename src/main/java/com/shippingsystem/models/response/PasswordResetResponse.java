package com.shippingsystem.models.response;

import lombok.Data;

@Data
public class PasswordResetResponse {
    String email;
    String status;
}
