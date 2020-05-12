package com.shippingsystem.models.response;

import lombok.Data;

@Data
public class ConfirmEmailResponse {
    private String email;
    private String status;
}
