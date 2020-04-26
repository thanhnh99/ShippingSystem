package com.shippingsystem.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseModel{

    @Column
    private String name;

    @ManyToMany
    @JoinTable(name = "user_role",//tạo ra bằng join
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> users;
}
