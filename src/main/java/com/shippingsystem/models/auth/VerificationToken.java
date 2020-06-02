package com.shippingsystem.models.auth;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table
public class VerificationToken  {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_VERIFIED = "VERIFIED";

    protected String token;
    protected String status;
    protected String userId;
    protected LocalDateTime expiryDateTime;
    protected LocalDateTime issuedDateTime;
    protected LocalDateTime confirmDateTime;

    public VerificationToken(){
        this.token = UUID.randomUUID().toString();
        this.issuedDateTime = LocalDateTime.now();
        this.expiryDateTime = this.issuedDateTime.plusDays(1);
        this.status = STATUS_PENDING;
    }

}
