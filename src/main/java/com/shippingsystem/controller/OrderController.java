package com.shippingsystem.controller;


import com.mservice.allinone.models.PayGateResponse;
import com.mservice.allinone.models.PaymentResponse;
import com.shippingsystem.models.entity.OrderStatus;
import com.shippingsystem.models.request.OrderRequest;
import com.shippingsystem.models.request.OrderStatusRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.services.ItemService;
import com.shippingsystem.services.OrderService;
import com.shippingsystem.services.OrderStatusService;
import com.shippingsystem.services.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
    Payment payment;

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
                                       @RequestBody OrderRequest orderRequest)
    {
        ResponseBaseModel response = new ResponseBaseModel();

        response = orderService.editOrder(orderId,orderRequest);

        if(response.getStatusCode().equals("200")) return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping
    public ResponseEntity getOrder(@RequestParam(value = "orderId", required = false) String orderId,
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

    @GetMapping("/payment/response/{order_id}/{request_id}")
    public ResponseEntity paymentResponse(@PathVariable String order_id,@PathVariable String request_id)
    {
        return payment.DisplayResultPayment(order_id,request_id);
    }
    @PostMapping(value = "/payment/response")
    public void IPNPayment(@RequestBody PayGateResponse response)
    {
        System.out.println("Gui NotifyUrl roi nhe");
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


    @PostMapping("/{order_id}/status")
    public ResponseEntity updateOrderStatus(@PathVariable(name = "order_id") String orderId,
                                            @RequestBody OrderStatusRequest orderStatusRequest)
    {
        ResponseOneModel<OrderStatus> response = orderStatusService.addOrderStatus(orderId, orderStatusRequest);

        if(response.getStatusCode().equals("200")) return ResponseEntity.ok().body(response);
        return ResponseEntity.status(203).body(response);
    }

}
