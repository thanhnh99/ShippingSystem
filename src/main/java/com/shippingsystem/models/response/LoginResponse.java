package com.shippingsystem.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse implements Serializable {
    private String token;
    private String status;
    private List role;
}
