package com.shippingsystem.controller;


import com.shippingsystem.models.Item;
import com.shippingsystem.models.Order;
import com.shippingsystem.models.OrderStatus;
import com.shippingsystem.models.requestModel.OrderRequest;
import com.shippingsystem.services.ItemService;
import com.shippingsystem.services.OrderService;
import com.shippingsystem.services.OrderStatusService;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderStatusService orderStatusService;

    @PostMapping
    public ResponseEntity addOrder(@RequestBody OrderRequest newOrder)
    {
        try {
            Item item = itemService.getInfo(newOrder.getItemType()).get();


            Order order = new Order();
            order.setName(newOrder.getName());
            order.setItem(item);
            order.setReceiveName(newOrder.getReceiveName());
            order.setReceiveAddress(newOrder.getReceiveAddress());
            order.setReceivePhone(newOrder.getReceivePhone());
//            order.setUser();
            order.setSendAddress(newOrder.getSendAddress());
            orderService.addOrder(order);
//            String status = orderService.addOrder();



            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setValue(1);
            orderStatus.setOrder(order);
            orderStatusService.addOrderStatus(orderStatus);

            return ResponseEntity.ok().body(order);

        }catch (NoSuchElementException e)
        {
            return ResponseEntity.status(404).body("Item not found");
        }

    }

//    @GetMapping
//    public List<Order> getAllOrder()
//    {
//        return orderService.getAllOrder();
//    }

    @GetMapping
    public ResponseEntity getOrder(@RequestParam(value = "orderId", required = false) Long orderId,
                                    @RequestParam(value = "local", required = false)String local)
    {
        if(orderId !=null)
        {
            Order order = null;
            order = orderService.getInfor(orderId);
            return ResponseEntity.ok().body(order);
        }
        List<Order> orders = null;
        if(local!=null)
        {
            orders = orderService.findByReceiveAddress(local);
            return ResponseEntity.ok().body(orders);
        }
        if(orderId == null && local ==null)
        {
            orders = orderService.getAllOrder();
            return ResponseEntity.ok().body(orders);
        }

        return   ResponseEntity.status(404).body("No order was founded");
    }

//    @PutMapping("/{order_id}")
//    public ResponseEntity editOrder(@RequestBody OrderRequest newOrder, @PathVariable(name = "order_id") Long id)
//    {
//
//        return (ResponseEntity) ResponseEntity.ok();
//    }

    @PutMapping("/{order_id}")
    public ResponseEntity updateStatusOrder(@RequestBody Long status,@PathVariable(name = "order_id") Long orderId)
    {
        return (ResponseEntity) ResponseEntity.ok().body(orderId);
    }

    @DeleteMapping("/{order_id}")
    public  ResponseEntity deleteOrder(@PathVariable(name = "order_id")Long orderId)
    {
        return (ResponseEntity) ResponseEntity.ok();
    }


}
