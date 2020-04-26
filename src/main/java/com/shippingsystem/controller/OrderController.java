package com.shippingsystem.controller;


import com.shippingsystem.models.Order;
import com.shippingsystem.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/")
    public ResponseEntity addOrder(@RequestBody Order order)
    {
//        orderService.addOrder(order);
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/{order_id}")
    public Optional<Order> getInfor(@PathVariable(name = "order_id") Long orderId)
    {
        return orderService.getInfor(orderId);
    }
}
