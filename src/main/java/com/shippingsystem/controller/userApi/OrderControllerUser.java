package com.shippingsystem.controller.userApi;

import ch.qos.logback.classic.Logger;
import com.shippingsystem.models.entity.Order;
import com.shippingsystem.models.entity.User;
import com.shippingsystem.models.request.IpnMomo;
import com.shippingsystem.models.request.OrderRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.services.*;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user/order")
public class OrderControllerUser {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
    Payment payment;

    @Autowired
    UserService userService;


    @GetMapping
    public ResponseEntity getOrders(HttpServletRequest request )
    {
        ResponseListModel response = new ResponseListModel();
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        String email =null;
        try {
            email = userService.getEmailFromToken(jwtToken);
            response = orderService.getOrderByEmail(email);
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

    @PostMapping
    public ResponseEntity addOrder(@RequestBody OrderRequest newOrder)
    {
        ResponseBaseModel response = new ResponseBaseModel();

        response = orderService.addOrder(newOrder);

        if(response.getStatusCode().equals("200")) return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PutMapping("{order_id}")
    public  ResponseEntity editOrder(@PathVariable(value = "order_id") String orderId,
                                     @RequestBody OrderRequest orderRequest,
                                     HttpServletRequest authen) {
        ResponseOneModel response = new ResponseOneModel();
        final String requestTokenHeader = authen.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        String email = null;
        try {
            email = userService.getEmailFromToken(jwtToken);
            response = orderService.editOrderInfo(orderId, orderRequest, email);
        } catch (Exception ex) {
            Logger logger = null;
            logger.error("failed on set user authentication", ex);
        }
        if (response.getStatusCode().equals("200")) {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(203).body(response);
    }

    @GetMapping("/payment/response/{order_id}/{request_id}")
    public ResponseEntity paymentResponse(@PathVariable String order_id,@PathVariable String request_id)
    {
        ResponseOneModel response = new ResponseOneModel();
        response.setData(payment.DisplayResultPayment(order_id,request_id));
        return ResponseEntity.ok(response);
    }
    @PostMapping(value = "/payment/response", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public void IPNPayment(IpnMomo response)
    {
        payment.IPNProcess(response);
    }

    @DeleteMapping("/{order_id}")
    public  ResponseEntity deleteOrder(@PathVariable(name = "order_id")String orderId)
    {
        ResponseBaseModel response = new ResponseBaseModel();
        response = orderService.deleteOrder(orderId);
        if(response.getStatusCode().equals("200")) return ResponseEntity.ok().body(response);
        return ResponseEntity.status(203).body(response);
    }

}
