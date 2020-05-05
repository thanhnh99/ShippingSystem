package com.shippingsystem.services;

import com.shippingsystem.models.Order;
import com.shippingsystem.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    IOrderRepository orderRepository ;

    public List<Order> getAllOrder()
    {
        return orderRepository.findAll().get();
    }

    public String addOrder(Order order)
    {
        try {
            orderRepository.save(order);
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
        return "SUCCESS";
    }

    public Order getInfor(Long id)
    {
        Order order = null;
        try{
            order =  orderRepository.findById(id).get();
        }catch (NoSuchElementException e)
        {
            throw e;
        }
        catch (NullPointerException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw e;
        }
        return order;
    }

    public List<Order> findByReceiveAddress(String local)
    {
        return orderRepository.findByReceiveAddress(local);
    }

}
