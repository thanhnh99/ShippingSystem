package com.shippingsystem.models.request;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class RegistrationRequest {
    @NotNull
    String email;
    @NotNull
    String username;
    @NotNull
    String password;
    @NotNull
    String address;
    @NotNull
    String phone;
}