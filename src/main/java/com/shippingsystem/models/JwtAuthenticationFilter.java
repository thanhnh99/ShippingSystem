package com.shippingsystem.models;

import com.shippingsystem.services.UserDetailServiceCustom;
import com.shippingsystem.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

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
            try {
                email = userService.getEmailFromToken(jwtToken);

            } catch (Exception ex) {
                logger.error("failed on set user authentication", ex);
            }
        } else {
            logger.warn("JWT does not begin with Bearer String");
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = this.userDetailServiceCustom.loadUserByEmail(email);

            if(userService.validateToken(email, jwtToken)){

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }

}
