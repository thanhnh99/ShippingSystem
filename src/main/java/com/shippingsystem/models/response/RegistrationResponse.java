package com.shippingsystem.models.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationResponse {
    private String email;
    private String password;
    private String status;

}
