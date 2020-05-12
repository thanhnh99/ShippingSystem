package com.shippingsystem.models.requestModel;

import com.shippingsystem.Enum.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequest {
    private Long stockId;
    private Long shipperId;
    private EOrderStatus status;
}
