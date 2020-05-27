package com.shippingsystem.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Getter
    @Setter
    @NotNull
    private String email;

    @Getter @Setter
    @NotNull
    private String password;
}