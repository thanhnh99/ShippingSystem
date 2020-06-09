package com.shippingsystem.services;

import com.shippingsystem.Enum.EOrderStatus;
import com.shippingsystem.models.entity.Order;
import com.shippingsystem.models.entity.OrderStatus;
import com.shippingsystem.models.entity.Stock;
import com.shippingsystem.models.request.OrderStatusRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.repository.IOrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStatusService {
    @Autowired
    private IOrderStatusRepository iOrderStatusRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockService stockService;

    public ResponseOneModel<OrderStatus> addOrderStatus(String orderId, OrderStatusRequest orderStatusRequest)
    {
        ResponseOneModel response = new ResponseOneModel();
        try {
            /**
             * Validate Request
             */
            Stock stock = null;
            if(orderStatusRequest.getStatus() == EOrderStatus.IN_STOCK)
            {
                if (orderStatusRequest.getStockId()==null)
                {
                    response.setStatusCode("411");
                    response.getMessage().setTitle("Stock is require!!!!");
                    response.setData(null);
                    return response;
                }
                else {
                    if(!stockService.findOneById(orderStatusRequest.getStockId()).getStatusCode().equals("200"))
                    {
                        response.setStatusCode(stockService.findOneById(orderStatusRequest.getStockId()).getStatusCode());
                        response.setMessage( stockService.findOneById(orderStatusRequest.getStockId()).getMessage());
                        return response;
                    }
                     stock =  stockService.findOneById(orderStatusRequest.getStockId()).getData();
                }
            }
            if(orderStatusRequest.getStatus() == EOrderStatus.SHIPPING)
            {
                if (orderStatusRequest.getShipperId()==null)
                {
                    response.setStatusCode("411");
                    response.getMessage().setTitle("Shipper is require!!!!");
                    response.setData(null);
                    return response;
                }
                else
                {
                    /**
                     * TODO: SET SHIPPER
                     */
//                    if(!UserService.findUserByEmail(orderStatusRequest.getShipperId()).equals("200"))
//                    {
//                        response.setStatusCode(stockService.findOneById(orderStatusRequest.getStockId()).getStatusCode());
//                        response.setMessage( stockService.findOneById(orderStatusRequest.getStockId()).getMessage());
//                        return response;
//                    }
//                    User user =  UserService(orderStatusRequest.getShipperId());
                }
            }

            if(orderService.findOneById(orderId).getStatusCode().equals("200"))
            {
                Order order = orderService.findOneById(orderId).getData();
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setValue(orderStatusRequest.getStatus());
                orderStatus.setStock(stock);

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

    public ResponseListModel<OrderStatus> getAllOrderStatus(String orderId) {
        ResponseListModel<OrderStatus> response = new ResponseListModel<>();
        List<OrderStatus> orderStatusList = null;

        try{
            orderStatusList = iOrderStatusRepository.getOrderStatusByOrderId(orderId);
        }
        catch (Exception e) {
            response.setStatusCode("500");
            response.getMessage().setTitle("ERROR");
            e.getStackTrace();
        }

        if(orderStatusList == null) {
            response.setStatusCode("203");
            response.getMessage().setTitle("HAVE NOT ORDER");
        } else {
            response.setStatusCode("200");
            response.getMessage().setTitle("SUCCESSFUL");
            response.setData(orderStatusList);
        }

        return response;
    }
}
