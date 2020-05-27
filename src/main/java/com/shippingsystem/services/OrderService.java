package com.shippingsystem.services;

import com.shippingsystem.Enum.EOrderStatus;
import com.shippingsystem.models.entity.Item;
import com.shippingsystem.models.entity.Order;
import com.shippingsystem.models.entity.OrderStatus;
import com.shippingsystem.models.request.OrderRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.repository.IItemRepository;
import com.shippingsystem.repository.IOrderRepository;
import com.shippingsystem.repository.IOrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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

    @Autowired
    ItemService itemService;

    @Autowired
    OrderService orderService;

    @Autowired
    Payment payment;

    public ResponseBaseModel addOrder(OrderRequest orderRequest)
    {
        ResponseBaseModel response = new ResponseBaseModel();
        ResponseOneModel<Item> itemResponse = itemService.getItemByCode(orderRequest.getItemCode());

        if(!itemResponse.getStatusCode().equals("200"))
        {
            response.setStatusCode("404");
            response.getMessage().setTitle("ItemStatus.CAN_NOT_FIND_ITEM");
            return response;
        }

        //Create Order
        Order order = new Order(orderRequest);
        order.setItem(itemResponse.getData());
        order.setPrice(BigDecimal.valueOf(10000000));

        //Create a OrderStatus for Order
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrder(order);
        orderStatus.setValue(EOrderStatus.PENDING);

        try {
            orderRepository.save(order);
        }
        catch (DataIntegrityViolationException e) {
            response.setStatusCode("404");
            response.getMessage().setTitle("ItemStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_SAVE");
            return response;
        }catch (EntityNotFoundException e) {
            orderRepository.delete(order);
            response.setStatusCode("203");
            response.getMessage().setTitle("ItemStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_FIND_ITEM");
        }
        catch (Exception e) {
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
        //TODO : redirect to payment momo
        String redirectLink = payment.DisplayPaymentScreen(order.getId());
        response.setStatusCode("200");
        response.getMessage().setTitle("successfully");
        response.getMessage().setContent(redirectLink);
        return response;

    }

    public ResponseOneModel editOrder(String orderId, OrderRequest orderRequest)
    {
        ResponseOneModel response = new ResponseOneModel();
        try {

            Order order = orderService.findOneById(orderId).getData();
            List<OrderStatus> orderStatusList = order.getOrderStatuses();
            if(orderStatusList.stream()
                    .filter(orderStatus -> !orderStatus.getValue().equals(EOrderStatus.PENDING))
                    .findFirst()
                    .isPresent())
            {
                response.setStatusCode("403");
                response.getMessage().setTitle("not access");
            }
            else {
                order.setName(orderRequest.getName());
                order.setReceiveName(orderRequest.getReceiveName());
                order.setReceiveAddress(orderRequest.getReceiveAddress());
                order.setReceivePhone(orderRequest.getReceivePhone());
                order.setSendAddress(orderRequest.getSendAddress());
                order.setItem(itemService.getItemByCode(orderRequest.getItemCode()).getData());
                orderRepository.save(order);

                response.setStatusCode("200");
                response.getMessage().setTitle("successfully");
                response.setData(order);
            }

        }catch (Exception e) {
            response.setStatusCode("203");
            response.getMessage().setTitle("Can't edit order");
        }
        return response;
    }

    public ResponseBaseModel deleteOrder(String id)
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

    public ResponseOneModel<Order> findOneById(String id)
    {
        ResponseOneModel response = new ResponseOneModel();
        try {
            Order order = orderRepository.findById(id).get();
            response.setStatusCode("200");
            response.getMessage().setTitle("OrderStatus.SUCCESSFULLY");
            response.setData(order);
        } catch (DataIntegrityViolationException e) {
            response.setStatusCode("404");
            response.getMessage().setTitle("OrderStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_GET");
            return response;        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("OrderStatus.CAN_NOT_GET_DATA");
        }
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
