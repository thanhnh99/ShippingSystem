package com.shippingsystem.controller.adminApi;

import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order")
public class OrderControllerAdmin {
    @Autowired
    OrderService orderService;

    @GetMapping()
    public ResponseEntity getOrders(@RequestParam(value = "orderId", required = false) String orderId,
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

}
