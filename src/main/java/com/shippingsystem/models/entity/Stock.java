package com.shippingsystem.models.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("orderStatus")
public class Stock extends BaseModel{


    @Column
    private String name;

    @Column
    private  String address;

    @Column
    private boolean status;

    @Column
    private double acreage;

    @Column
    private long totalOrder;

    @OneToMany(mappedBy= "stock", cascade = CascadeType.ALL)
    private Collection<Order> orders;
}
