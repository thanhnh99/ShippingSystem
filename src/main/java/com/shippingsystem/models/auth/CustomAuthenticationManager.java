package com.shippingsystem.models.auth;

import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() + "";
        String password = authentication.getPrincipal() + "";
        List<GranAuthorityImpl> granAuthorities = new ArrayList<>();
        granAuthorities.add(new GranAuthorityImpl("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(username, password,granAuthorities);
    }
}