package com.shippingsystem.models.entity;

import com.shippingsystem.models.request.OrderRequest;
import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//@Data//lombok
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity//Đánh dấu đây là table trong db
@Table//config db. Không có gì mặc định là defaule

public class Order extends BaseModel {

    @Column
    private String name;

    @Column
    private String receiveName;

    @Column
    private Date sendTime;

    @Column
    private Date completeTime;

    @Column
    private String sendAddress;

    @Column
    private String receiveAddress;

    @Column
    private String sendPhone;

    @Column
    private String receivePhone;

    @Column
    private BigDecimal price;

    @ManyToOne//nhiều order do 1 thằng user đặt
    @JoinColumn(name = "user_id")//khóa ngoại liên kết tới bảng user
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatus> orderStatuses;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public Order (OrderRequest orderRequest)
    {
        this.setName(orderRequest.getName());
        this.setReceiveName(orderRequest.getReceiveName());
        this.setReceiveAddress(orderRequest.getReceiveAddress());
        this.setReceivePhone(orderRequest.getReceivePhone());
        this.setSendAddress(orderRequest.getSendAddress());
    }
}
