package com.shippingsystem.services;

import com.shippingsystem.models.Order;
import com.shippingsystem.models.OrderStatus;
import com.shippingsystem.models.Stock;
import com.shippingsystem.models.requestModel.OrderStatusRequest;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.repository.IOrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class OrderStatusService {
    @Autowired
    private IOrderStatusRepository iOrderStatusRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockService stockService;

    public ResponseOneModel<OrderStatus> addOrderStatus(Long orderId, OrderStatusRequest orderStatusRequest)
    {
        ResponseOneModel response = new ResponseOneModel();
        try {
            if(orderService.findOneById(orderId).getStatusCode().equals("200"))
            {
                String oneModel = orderService.findOneById(orderId).getStatusCode();
                Order order = orderService.findOneById(orderId).getData();
                //TODO: set user
                Stock stock =  stockService.findOneById(orderStatusRequest.getStockId()).getData();
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
}
