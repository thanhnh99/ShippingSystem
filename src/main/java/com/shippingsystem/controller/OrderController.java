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


        return ResponseEntity.ok().body(newOrder);
    }

    @GetMapping("")
    public List<Order> getAllOrder()
    {
        return orderService.getAllOrder();
    }

//    @GetMapping("")
//    public Optional<Order> getInfor(@RequestParam(value = "orderId") Long orderId)
//    {
//        return orderService.getInfor(orderId);
//    }

//    @GetMapping("")
//    public ResponseEntity findOrderByLocal(@RequestParam(value = "local")String local)
//    {
//        List<Order> orders = orderService.findByReceiveAddress(local);
//        return ResponseEntity.ok().body(orders);
//    }

//    @PutMapping("{order_id}")
//    public ResponseEntity editOrder(@RequestBody OrderRequest newOrder, @PathVariable(name = "order_id") Long id)
//    {
//        if(orderService.getInfor(id) !=null)
//        {
//
//        }
//        return (ResponseEntity) ResponseEntity.notFound();
//    }

    @PutMapping("{order_id}")
    public ResponseEntity updateStatusOrder(@RequestBody Long status,@PathVariable(name = "order_id") Long orderId)
    {
        return (ResponseEntity) ResponseEntity.ok();
    }

    @DeleteMapping("/{order_id}")
    public  ResponseEntity deleteOrder(@PathVariable(name = "order_id")Long orderId)
    {
        return (ResponseEntity) ResponseEntity.ok();
    }


}
