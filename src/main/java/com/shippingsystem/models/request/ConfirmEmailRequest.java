package com.shippingsystem.models.request;

import lombok.Data;

@Data
public class ConfirmEmailRequest {
    private String token;
}
