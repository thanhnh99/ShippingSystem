package com.shippingsystem.models.request;

import com.shippingsystem.Enum.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequest {
    private String stockId;
    private String shipperId;
    private EOrderStatus status;
}
