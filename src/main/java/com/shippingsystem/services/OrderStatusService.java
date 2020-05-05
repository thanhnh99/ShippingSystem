package com.shippingsystem.services;

import com.shippingsystem.models.Order;
import com.shippingsystem.models.OrderStatus;
import com.shippingsystem.repository.IOrderRepository;
import com.shippingsystem.repository.IOrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderStatusService {
    @Autowired
    private IOrderStatusRepository iOrderStatusRepository;

    public Optional<OrderStatus> getInfo(Long id)
    {
        return iOrderStatusRepository.findById(id);
    }

    public  void addOrderStatus(OrderStatus orderStatus)
    {
            iOrderStatusRepository.save(orderStatus);
    }
}
