package com.shippingsystem.services;

import com.shippingsystem.models.UserDetailCustom;
import io.jsonwebtoken.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
@NoArgsConstructor
public class JwtTokenProvider {

    private final String JWT_SECRET = "lBkt8u0eBIKr0";

    private final long JWT_EXPIRATION = 60480000000L;

    /** tạo chuỗi token */
    public String generateToken(UserDetailCustom userDetailCustom){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(userDetailCustom.getUser().getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }


    public String getEmailFromToken(String token) throws NoSuchAlgorithmException {

        return getClaimFromToken(token, Claims::getSubject);

    }

    public Date getExpirationDateFromToken(String token) throws NoSuchAlgorithmException {

        return getClaimFromToken(token, Claims::getExpiration);

    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws NoSuchAlgorithmException {

        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);

    }
    private Boolean isTokenExpired(String token) throws NoSuchAlgorithmException {

        final Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());

    }



    public Boolean validateToken(String token, String email) throws NoSuchAlgorithmException {
        final String username = getEmailFromToken(token);
        return (username.equals(email) && !isTokenExpired(token));

    }
    private Claims getAllClaimsFromToken(String token) throws  NoSuchAlgorithmException {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();


    }


}
