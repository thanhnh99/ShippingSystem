package com.shippingsystem.services;

import ch.qos.logback.classic.Logger;
import com.shippingsystem.Enum.EOrderStatus;
import com.shippingsystem.models.entity.Item;
import com.shippingsystem.models.entity.Order;
import com.shippingsystem.models.entity.OrderStatus;
import com.shippingsystem.models.entity.User;
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
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    UserService userService;



    public ResponseBaseModel addOrder(OrderRequest orderRequest, HttpServletRequest authen)
    {
        ResponseBaseModel response = new ResponseBaseModel();
        ResponseOneModel<Item> itemResponse = itemService.getItemByCode(orderRequest.getItemCode());

        if(!itemResponse.getStatusCode().equals("200"))
        {
            response.setStatusCode("404");
            response.getMessage().setTitle("ItemStatus.CAN_NOT_FIND_ITEM");
            return response;
        }

        final String requestTokenHeader = authen.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        String email =null;
        try {
            email = userService.getEmailFromToken(jwtToken);
            User user = userService.findUserByEmail(email);

            //Create Order
            Order order = new Order(orderRequest);
            order.setItem(itemResponse.getData());
            order.setPrice(BigDecimal.valueOf(50000*itemResponse.getData().getMultiplicity()));
            order.setUser(user);

            //Save order
            orderRepository.save(order);

            //Create default OrderStatus for Order
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setOrder(order);
            orderStatus.setConfirmed(false);
            orderStatus.setConfirmedBy(user.getId());
            orderStatus.setValue(EOrderStatus.PENDING);

            //Save order status
            orderStatusRepository.save(orderStatus);

            //Create payment
            String redirectLink = payment.DisplayPaymentScreen(order.getId());
            response.setStatusCode("200");
            response.getMessage().setTitle("successfully");
            response.getMessage().setContent(redirectLink);

        }
        catch (DataIntegrityViolationException e) {
            response.setStatusCode("404");
            response.getMessage().setTitle("ItemStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_SAVE");
            return response;
        }catch (EntityNotFoundException e) {
            response.setStatusCode("203");
            response.getMessage().setTitle("ItemStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_FIND_ITEM");
        }catch (Exception ex) {
            Logger logger = null;
            logger.error("failed on set user authentication", ex);
        }
        return response;

    }

    public ResponseOneModel editOrderInfo(String orderId, OrderRequest request, String email)
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
                List<Order> ordersByEmail = this.getOrderByEmail(email).getData();
                for(int i = 0; i<ordersByEmail.size();i++)
                {
                    if(ordersByEmail.get(i).getId().equals(orderId))
                    {
                        ordersByEmail.get(i).setName(request.getName());
                        ordersByEmail.get(i).setReceiveAddress(request.getReceiveAddress());
                        ordersByEmail.get(i).setReceiveName(request.getReceiveName());
                        ordersByEmail.get(i).setReceivePhone(request.getReceivePhone());
                        ordersByEmail.get(i).setSendAddress(request.getSendAddress());
                        ordersByEmail.get(i).setUpdated_at(new Date());
                        ordersByEmail.get(i).setUpdated_by(userService.findUserByEmail(email).getId());
                        ordersByEmail.get(i).setSendPhone(request.getSendName());
                        orderRepository.save(ordersByEmail.get(i));
                        break;
                    }
                }

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
            List<OrderStatus> orderStatuses = orderStatusRepository.getOrderStatusByOrderId(id);
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

    public  ResponseListModel getOrderByEmail(String email)
    {
        ResponseListModel response = new ResponseListModel<>();
        try {
            User user = userService.findUserByEmail(email);
            List<Order> orders = (List<Order>) user.getOrders();
            response.setStatusCode("200");
            response.getMessage().setTitle("Successfully");
            response.setData(orders);
            return response;
        }catch (Exception e)
        {
            response.setStatusCode("400");
            response.getMessage().setTitle("Bad request getOrderByEmail!!!");
            response.setData(null);
            return response;
        }
    }


    //Lấy ra những order mà thằng Shipper đã nhận giao hàng
    public  ResponseListModel getOrderShipperByEmail(String email)
    {
        ResponseListModel response = new ResponseListModel<>();
        try {
            List<Order> orders = orderRepository.findOrderOfShipper(email);
            response.setStatusCode("200");
            response.getMessage().setTitle("Successfully");
            response.setData(orders);
            return response;
        }catch (Exception e)
        {
//            response.setStatusCode("400");
//            response.getMessage().setTitle("Bad request getOrderByEmail!!! file OrderService.java; line " +e.getStackTrace()[0].getLineNumber());
//            response.setData(null);
//            return response;
            e.getStackTrace();
            return response;
        }
    }
    /**
     * Lấy ra những order mà Shipper có thể nhận giao hàng.
     */
    public ResponseListModel getResponsibleAbleOrder(String sendAddress, String receiveAddress)
    {
        ResponseListModel response = new ResponseListModel<>();
        List<Order> orders = new ArrayList<Order>();
        try {

            if(sendAddress == null && receiveAddress != null)
            {
                orders = orderRepository.findByReceiveAddressNotComplete(receiveAddress);
            }
            else if(sendAddress != null && receiveAddress == null)
            {
                orders = orderRepository.findBySendAddressNotComplete(receiveAddress);
            }
            else if(sendAddress != null && receiveAddress != null)
            {
                orders = orderRepository.findBySendAddressAndReceiveAddressNotComplete(sendAddress,receiveAddress);
            }
            else
            {
                orders = orderRepository.findOrderNotComplete();
            }
            response.setStatusCode("200");
            response.getMessage().setTitle("Successfully");
            response.setData(orders);
            return response;
        }catch (Exception e)
        {
            response.setStatusCode("400");
            response.getMessage().setTitle("Bad request getResponsibleAbleOrder!!! file OrderService.java; line " +e.getStackTrace()[0].getLineNumber());
            response.setData(null);
            return response;
        }
    }

    public ResponseOneModel ResponsibleOrder(String order_id,String email)
    {
        ResponseOneModel response = new ResponseOneModel();
        try {
            List<OrderStatus> orderStatuses = orderStatusRepository.getOrderStatusByOrderId(order_id);
            if(orderStatuses.get(orderStatuses.size()-1).getValue().equals(EOrderStatus.SHIPPING)||
                    orderStatuses.get(orderStatuses.size()-1).getValue().equals(EOrderStatus.CANCEL)||
                    orderStatuses.get(orderStatuses.size()-1).getValue().equals(EOrderStatus.PICKUP)||
                    orderStatuses.get(orderStatuses.size()-1).getValue().equals(EOrderStatus.COMPLETE))
            {
                response.setStatusCode("403");
                response.getMessage().setTitle("not access");
                response.setData(null);
            }
            else
            {
                Order order = orderRepository.findById(order_id).get();
                User shipper = userService.findUserByEmail(email);
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setOrder(order);
                orderStatus.setValue(EOrderStatus.PICKUP);
                orderStatus.setShipper(shipper);
                orderStatus.setStock(null);
                orderStatus.setCreated_by(shipper.getId());
                orderStatusRepository.save(orderStatus);
                response.setStatusCode("200");
                response.getMessage().setTitle("successfully");
                response.setData(orderStatus);
            }
        }catch (Exception e)
        {
            response.setStatusCode("400");
            response.getMessage().setTitle("Exception!!!! file OrderService, line "+e.getStackTrace()[0].getLineNumber());
            response.setData(null);
        }
        return response;
    }

    public ResponseOneModel Warehousing(String order_id,String email)
    {
        ResponseOneModel response = new ResponseOneModel();
        try {
            List<OrderStatus> orderStatuses = orderStatusRepository.getOrderStatusByOrderId(order_id);
            if(!orderStatuses.get(orderStatuses.size()-1).getValue().equals(EOrderStatus.SHIPPING)||
                    !orderStatuses.get(orderStatuses.size()-1).getValue().equals(EOrderStatus.PICKUP))
            {
                response.setStatusCode("403");
                response.getMessage().setTitle("not access");
                response.setData(null);
            }
            else
            {
                Order order = orderRepository.findById(order_id).get();
                User shipper = userService.findUserByEmail(email);
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setOrder(order);
                orderStatus.setValue(EOrderStatus.IN_STOCK);
                orderStatus.setShipper(shipper);
                orderStatus.setStock(null);
                orderStatus.setCreated_by(shipper.getId());
                orderStatusRepository.save(orderStatus);
                response.setStatusCode("200");
                response.getMessage().setTitle("successfully");
                response.setData(orderStatus);
            }
        }catch (Exception e)
        {
            response.setStatusCode("400");
            response.getMessage().setTitle("Exception!!!! file OrderService, line "+e.getStackTrace()[0].getLineNumber()+e.getMessage());
            response.setData(null);
        }
        return response;
    }

}
