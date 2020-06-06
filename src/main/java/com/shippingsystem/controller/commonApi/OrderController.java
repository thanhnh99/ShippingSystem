package com.shippingsystem.controller.commonApi;

import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")

public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("{order_id}")
    public ResponseEntity getOrder(@PathVariable String orderId)
    {
        ResponseOneModel response = orderService.findOneById(orderId);
        if(response.getStatusCode().equals("200"))
        {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(203).body(response);
    }
}
