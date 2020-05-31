package com.shippingsystem.services;

import com.shippingsystem.Enum.EOrderStatus;
import com.shippingsystem.models.Order;
import com.shippingsystem.models.OrderStatus;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.repository.IOrderRepository;
import com.shippingsystem.repository.IOrderStatusRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

@Service
public class OrderStatusService {
    @Autowired
    private IOrderStatusRepository iOrderStatusRepository;

    @Autowired
    private OrderService orderService;

    public Optional<OrderStatus> getInfo(Long id)
    {
        return iOrderStatusRepository.findById(id);
    }

    public ResponseOneModel<OrderStatus> addOrderStatus(Long orderId, EOrderStatus orderStatusRequest)
    {
        ResponseOneModel response = new ResponseOneModel();
        try {
            if(orderService.findOneById(orderId).getStatusCode().equals("200"))
            {
                Order order = orderService.findOneById(orderId).getData();
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setValue(orderStatusRequest);
                orderStatus.setOrder(order);
                iOrderStatusRepository.save(orderStatus);

                response.setStatusCode("200");
                response.getMessage().setTitle("OrderStatus.SUCCESSFULLY");
                response.setData(orderStatus);
            }
            else
            {
                response.setStatusCode("404");
                response.getMessage().setTitle("OrderStatus.Order does not exist!!!");
                response.setData(null);
            }

        }catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("Can't change order status");
        }
        return response;
    }

    public  void addOrderStatus(OrderStatus orderStatus)
    {
            iOrderStatusRepository.save(orderStatus);
    }
}
