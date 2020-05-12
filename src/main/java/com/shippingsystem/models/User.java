package com.shippingsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseModel{

    @Column
    private String email;

    @Column
    private  String username;

    @Column
    private String  address;

    @Column
    private String password;

    @Column
    private boolean isEnable;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Collection<Order> orders;

    @OneToOne(mappedBy = "shipper")
    private OrderStatus orderStatus;

    @ManyToMany(mappedBy = "users")
    private Collection<Role> roles;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
