package com.shippingsystem.models;


import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatus extends BaseModel {

    @Column
    private int value;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Order order;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="warehouse_id", referencedColumnName = "id",nullable = true)
    private Warehouse warehouse;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="shipper_id", referencedColumnName = "id", nullable = true)
    private User shipper;

}
