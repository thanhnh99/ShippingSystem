package com.shippingsystem.models.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class GranAuthorityImpl implements GrantedAuthority {

    private String authority;

    public static final String ROLE_USER = "ROLE_USER";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
