package com.shippingsystem.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data//lombok
@AllArgsConstructor
@NoArgsConstructor
@Entity//Đánh dấu đây là table trong db
@Table//config db. Không có gì mặc định là defaule
public class Order {
    @Id//đánh dấu primary key trong db
    @GeneratedValue(strategy = GenerationType.IDENTITY)//tự động tăng id
    private Long id;

    @Column
    private String name;

    @Column
    private Date sendTime;

    @Column
    private Date completeTime;

    @Column
    private BigDecimal price;

    @ManyToOne//nhiều order do 1 thằng user đặt
    @JoinColumn(name = "user_id")//khóa ngoại liên kết tới bảng user
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatus> orderStatuses;
}
