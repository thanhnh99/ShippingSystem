package com.shippingsystem.controller.commonApi;

import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.services.OrderService;
import com.shippingsystem.services.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("common/order")

public class OrderController {
    @Autowired
    OrderStatusService orderStatusService;

    @GetMapping()
    public ResponseEntity getOrder(@RequestParam(name = "order_id",required = true) String orderId)
    {
        ResponseListModel response = orderStatusService.getAllOrderStatus(orderId);

        if(response.getStatusCode().equals("203")) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
