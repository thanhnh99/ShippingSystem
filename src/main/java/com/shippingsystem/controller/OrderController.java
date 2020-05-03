package com.shippingsystem.controller;


import com.shippingsystem.models.Item;
import com.shippingsystem.models.Order;
import com.shippingsystem.models.requestModel.OrderRequest;
import com.shippingsystem.services.ItemService;
import com.shippingsystem.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @PostMapping("")
    public ResponseEntity addOrder(@RequestBody OrderRequest newOrder)
    {
//        Order order = new Order();
//        order.setName(newOrder.getName());
//        order.setName(newOrder.getName());
//        order.setName(newOder.getName());
//        order.setItem(newOder.getItem());
//        orderService.addOrder(order);

        return ResponseEntity.ok().body(newOrder);
    }

    @GetMapping("")
    public List<Order> getAllOrder()
    {
        return orderService.getAllOrder();
    }

    @GetMapping("/{order_id}")
    public Optional<Order> getInfor(@PathVariable(name = "order_id") Long orderId)
    {
        return orderService.getInfor(orderId);
    }

    @PutMapping("{order_id}")
    public ResponseEntity editOrder(@RequestBody Order newOrder, @PathVariable(name = "order_id") Long id)
    {
        if(orderService.getInfor(id) !=null)
        {
            orderService.getInfor(id)
                    .map(order -> {
                        order.setName(newOrder.getName());
                        order.setItem(newOrder.getItem());
                        return ResponseEntity.ok().body(order);
                    });
        }
        return (ResponseEntity) ResponseEntity.notFound();
    }


}
