package com.shippingsystem.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseModel {

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String address;

    @Column
    private String password;

    @Column
    private boolean isEnable;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Order> orders;

    @OneToOne(mappedBy = "shipper")
    private OrderStatus orderStatus;

    @ManyToMany(mappedBy = "users")
    private Collection<Role> roles;

    public User(String email, String username, String password, String adress,Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = adress;
        this.roles = new ArrayList<Role>();
        this.roles.add(role);
    }
}
