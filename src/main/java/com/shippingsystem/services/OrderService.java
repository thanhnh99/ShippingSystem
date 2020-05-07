package com.shippingsystem.services;

import com.shippingsystem.models.Item;
import com.shippingsystem.models.Order;
import com.shippingsystem.models.OrderStatus;
import com.shippingsystem.models.StockStatus;
import com.shippingsystem.models.requestModel.OrderRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.repository.IItemRepository;
import com.shippingsystem.repository.IOrderRepository;
import com.shippingsystem.repository.IOrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    IOrderRepository orderRepository ;

    @Autowired
    IItemRepository itemRepository ;

    @Autowired
    IOrderStatusRepository orderStatusRepository ;

    public ResponseBaseModel addOrder(OrderRequest orderRequest)
    {
        ResponseBaseModel response = new ResponseBaseModel();

        Item item = itemRepository.getOne(orderRequest.getItemType());
        if(item == null)
        {
            response.setStatusCode("404");
            response.getMessage().setTitle("Không tìm thấy item");
        }

        //Create Order
        Order order = new Order();
        order.setName(orderRequest.getName());
        order.setReceiveName(orderRequest.getReceiveName());
        order.setReceiveAddress(orderRequest.getReceiveAddress());
        order.setReceivePhone(orderRequest.getReceivePhone());
        order.setSendAddress(orderRequest.getSendAddress());
        order.setItem(item);
        order.setPrice(BigDecimal.valueOf(10000000));

        //Create a OrderStatus for Order
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrder(order);

        try {
            orderRepository.save(order);
        } catch (Exception e) {
            response.setStatusCode("203");
            response.getMessage().setTitle("Can't save order");
        }
        try {
            orderStatusRepository.save(orderStatus);
        } catch (Exception e) {
            orderRepository.delete(order);
            response.setStatusCode("203");
            response.getMessage().setTitle("Can't save order");
        }

        response.setStatusCode("200");
        response.getMessage().setTitle("successfully");
        return response;

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

    public ResponseOneModel findOneById(Long id)
    {
        ResponseOneModel response = new ResponseOneModel();
        Order order = null;
        try {
            order = orderRepository.getOne(id);
        } catch (DataIntegrityViolationException e) {
            response.setStatusCode("404");
            response.getMessage().setTitle("OrderStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_GET");
            return response;        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("OrderStatus.CAN_NOT_GET_DATA");
        }
        response.setStatusCode("200");
        response.getMessage().setTitle("OrderStatus.SUCCESSFULLY");
        response.setData(order);
        return response;
    }

    public ResponseListModel findByReceiveAddress(String local)
    {
        ResponseListModel response = new ResponseListModel();
        List<Order> orders = null;
        try{
            orders = (List<Order>) orderRepository.findByReceiveAddress(local);
        } catch (DataIntegrityViolationException e) {
            response.setStatusCode("404");
            response.getMessage().setTitle("OrderStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_GET");
            return response;        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("OrderStatus.CAN_NOT_GET_DATA");
        }
        response.setStatusCode("200");
        response.getMessage().setTitle("ItemStatus.SUCCESSFULLY");
        response.setData(orders);
        return response;
    }

    public ResponseListModel getAll()
    {
        ResponseListModel response = new ResponseListModel();
        List<Order> orders = null;
        try{
            orders = orderRepository.findAll();
        } catch (DataIntegrityViolationException e) {
            response.setStatusCode("404");
            response.getMessage().setTitle("OrderStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_GET");
            return response;        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("OrderStatus.CAN_NOT_GET_DATA");
        }
        response.setStatusCode("200");
        response.getMessage().setTitle("OrderStatus.SUCCESSFULLY");
        response.setData(orders);
        return response;
    }

}
