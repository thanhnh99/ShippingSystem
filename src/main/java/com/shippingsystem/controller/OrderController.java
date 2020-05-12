package com.shippingsystem.controller;


import com.shippingsystem.Enum.EOrderStatus;
import com.shippingsystem.models.Item;
import com.shippingsystem.models.Order;
import com.shippingsystem.models.OrderStatus;
import com.shippingsystem.models.requestModel.OrderRequest;
import com.shippingsystem.models.requestModel.OrderStatusRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.services.ItemService;
import com.shippingsystem.services.OrderService;
import com.shippingsystem.services.OrderStatusService;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
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
        ResponseBaseModel response = new ResponseBaseModel();

        response = orderService.addOrder(newOrder);

        if(response.getStatusCode().equals("200")) return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @PutMapping("{order_id}")
    public  ResponseEntity editOrder(@PathVariable(value = "order_id") Long orderId,
                                       @RequestBody OrderRequest orderRequest)
    {
        ResponseBaseModel response = new ResponseBaseModel();

        response = orderService.editOrder(orderId,orderRequest);

        if(response.getStatusCode().equals("200")) return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping
    public ResponseEntity getOrder(@RequestParam(value = "orderId", required = false) Long orderId,
                                    @RequestParam(value = "sendAddress", required = false)String sendAddress,
                                   @RequestParam(value = "receiveAddress", required = false)String receiveAddress)
    {
        if(orderId!=null)
        {
            ResponseOneModel response = orderService.findOneById(orderId);
            if(response.getStatusCode().equals("200"))
            {
                return ResponseEntity.ok().body(response);
            }
            return ResponseEntity.status(203).body(response);

        }
        if(receiveAddress!=null)
        {
            ResponseListModel response = orderService.findByReceiveAddress(receiveAddress);
            if(response.getStatusCode().equals("200"))
            {
                return ResponseEntity.ok().body(response);
            }
            return ResponseEntity.status(203).body(response);
        }

        ResponseListModel response = orderService.getAll();
        if(response.getStatusCode().equals("200"))
        {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(203).body(response);

    }


    @DeleteMapping("/{order_id}")
    public  ResponseEntity deleteOrder(@PathVariable(name = "order_id")Long orderId)
    {
        ResponseBaseModel response = new ResponseBaseModel();
        response = orderService.deleteOrder(orderId);
        if(response.getStatusCode().equals("200")) return ResponseEntity.ok().body(response);
        return ResponseEntity.status(203).body(response);
    }


    @PostMapping("/{order_id}/status")
    public ResponseEntity updateOrderStatus(@PathVariable(name = "order_id") Long orderId,
                                            @RequestBody OrderStatusRequest orderStatusRequest)
    {
        ResponseOneModel<OrderStatus> response = orderStatusService.addOrderStatus(orderId, orderStatusRequest);

        if(response.getStatusCode().equals("200")) return ResponseEntity.ok().body(response);
        return ResponseEntity.status(203).body(response);
    }

}
