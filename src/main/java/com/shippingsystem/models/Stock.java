package com.shippingsystem.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("orderStatus")
public class Stock extends BaseModel{

    public Stock(String name, String address, int status) {
        this.name = name;
        this.address = address;
        this.status = status;
    }

    @Column
    private String name;

    @Column
    private  String address;

    @Column
    private int status;

    @OneToOne(mappedBy = "stock")
    private OrderStatus orderStatus;
}
