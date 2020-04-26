package com.shippingsystem.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse extends BaseModel{

    @Column
    private String name;

    @Column
    private  String address;

    @Column
    private int status;

    @OneToOne(mappedBy = "warehouse")
    private OrderStatus orderStatus;
}
