package com.shippingsystem.models;


import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private int value;

    @ManyToOne
    @JoinColumn(name="order_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Order order;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;
}
