package com.shippingsystem.models.requestModel;

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

}