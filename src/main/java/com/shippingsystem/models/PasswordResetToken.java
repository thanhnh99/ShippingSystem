package com.shippingsystem.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
public class PasswordResetToken extends Token {

    @Id
    private String id;

    public PasswordResetToken(){
        super();
    }

}
