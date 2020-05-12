package com.shippingsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
public class Token {
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_VERIFIED = "VERIFIED";

    protected String token;
    protected String status;
    protected String userId;
    protected LocalDateTime expiryDateTime;
    protected LocalDateTime issuedDateTime;
    protected LocalDateTime confirmDateTime;

    public Token(){
        this.token = UUID.randomUUID().toString();
        this.issuedDateTime = LocalDateTime.now();
        this.expiryDateTime = this.issuedDateTime.plusDays(1);
        this.status = STATUS_PENDING;
    }
}
