package com.shippingsystem.services;

import com.shippingsystem.models.Order;
import com.shippingsystem.models.requestModel.OrderRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    IOrderRepository orderRepository ;

    public List<Order> getAllOrder()
    {
        return (List<Order>) orderRepository.findAll();
    }

//    public OrderResponse addOrder(OrderRequest orderRequest)
//    {
//        //timf ra 1 item
//
//        OrderResponse response = new OrderResponse();
//        ResponseBaseModel response = new ResponseBaseModel();
//        Order order = new Order();
//        order.setName(orderRequest.getName());
////        order.setItem(item);
//        order.setReceiveName(orderRequest.getReceiveName());
//        order.setReceiveAddress(orderRequest.getReceiveAddress());
//        order.setReceivePhone(orderRequest.getReceivePhone());
//
//
//        return response;
//    }

//    public ResponseBaseModel getOrderInfor(Long id)
//    {
//
//    }

    public List<Order> findByReceiveAddress(String local)
    {
        return orderRepository.findByReceiveAddress(local);
    }

    public ResponseBaseModel deleteOrder(Long id)
    {
        ResponseBaseModel response = new ResponseBaseModel();
        try {
            orderRepository.deleteById(id);
        }
        catch (NullPointerException e)
        {
            response.setStatusCode("404");
            response.getMessage().setTitle("OrderStatus.ORDER_NOT_FOUND!_CAN_NOT_DELETE");
            return response;
        }
        catch (DataIntegrityViolationException e) {
            response.setStatusCode("404");
            response.getMessage().setTitle("OrderStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_DELETE");
            return response;        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("OrderStatus.CAN_NOT_DELETE_DATA");
        }
        response.setStatusCode("200");
        response.getMessage().setTitle("ItemStatus.SUCCESSFULLY");
        return response;
    }
}
