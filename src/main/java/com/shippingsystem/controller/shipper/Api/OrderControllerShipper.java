package com.shippingsystem.controller.shipper.Api;

import ch.qos.logback.classic.Logger;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.services.OrderService;
import com.shippingsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/shipper/order")

public class OrderControllerShipper {
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @GetMapping
    public ResponseEntity getShipOrders(HttpServletRequest request )
    {
        ResponseListModel response = new ResponseListModel();
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        String email =null;
        try {
            email = userService.getEmailFromToken(jwtToken);
            response = orderService.getOrderShipperByEmail(email);
        } catch (Exception ex) {
            Logger logger = null;
            logger.error("failed on set user authentication", ex);
        }
        if(response.getStatusCode().equals("200"))
        {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(203).body(response);
    }

    @GetMapping("responsibleAble")
    public ResponseEntity getOrderResponsibleAble(@RequestParam(value = "sendAddress", required = false)String sendAddress,
                                                  @RequestParam(value = "receiveAddress", required = false)String receiveAddress)
    {
        ResponseListModel response = new ResponseListModel();
        try {
            response = orderService.getResponsibleAbleOrder(sendAddress,receiveAddress);
        } catch (Exception ex) {
            Logger logger = null;
            logger.error("can't getOrderResponsibleAble", ex);
        }
        if(response.getStatusCode().equals("200"))
        {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(203).body(response);
    }

    @PostMapping("responsibleAble")
    public ResponseEntity ResponsibleOrder(String orderId,HttpServletRequest request )
    {
        ResponseOneModel response = new ResponseOneModel();
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            String jwtToken = requestTokenHeader.substring(7);
            String email = userService.getEmailFromToken(jwtToken);
            response = orderService.ResponsibleOrder(orderId,email);
        } catch (Exception ex) {
            Logger logger = null;
            logger.error("can't ResponsibleOrder", ex);
        }
        if(response.getStatusCode().equals("200"))
        {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(203).body(response);
    }

}
