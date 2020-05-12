package com.shippingsystem.models.response;

import lombok.Data;

@Data
public class NewPasswordResponse {
    private String email;
    private String status;
}
