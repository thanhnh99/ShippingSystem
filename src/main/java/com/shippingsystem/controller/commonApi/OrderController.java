package com.shippingsystem.controller.commonApi;

import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("common/order")

public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping()
    public ResponseEntity getOrder(@RequestParam(name = "order_id",required = true) String orderId)
    {
        ResponseOneModel response = orderService.findOneById(orderId);
        if(response.getStatusCode().equals("200"))
        {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(203).body(response);
    }
}
