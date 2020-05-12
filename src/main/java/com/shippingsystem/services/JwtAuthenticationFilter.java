package com.shippingsystem.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import web.service.grpc.user.GetEmailRequest;
import web.service.grpc.user.ValidateTokenRequest;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailServiceCustom userDetailServiceCustom;


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) {
        final String requestTokenHeader = request.getHeader("Authorization");

        String email = null;
        String jwtToken = null;

        if(requestTokenHeader != null) {
            jwtToken = requestTokenHeader.substring(7);
            System.out.println(jwtToken);
            try {
                GetEmailRequest.Builder getEmailRequest = GetEmailRequest.newBuilder();
                getEmailRequest.setToken(jwtToken);
                email = grpcClientService.getEmailFromToken(getEmailRequest.build());

            } catch (Exception ex) {
                logger.error("failed on set user authentication", ex);
            }
        } else {
            logger.warn("JWT does not begin with Bearer String");
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = this.userDetailServiceCustom.loadUserByEmail(email);
            ValidateTokenRequest.Builder validateRequest = ValidateTokenRequest.newBuilder();
            validateRequest.setEmail(email);
            validateRequest.setToken(jwtToken);

            if(grpcClientService.validateToken(validateRequest.build())){

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }

}
