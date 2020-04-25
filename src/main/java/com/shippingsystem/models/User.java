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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private  String username;

    @Column
    private String  address;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Collection<Order> orders;

    @OneToOne(mappedBy = "shipper")
    private OrderStatus orderStatus;

    @ManyToMany(mappedBy = "users")
    private Collection<Role> roles;
}