package com.shippingsystem.models.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties("users")
public class Role extends BaseModel{

    @Column
    private String roleName;

    @ManyToMany
    @JoinTable(name = "user_role",//tạo ra bằng join
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> users;
}
