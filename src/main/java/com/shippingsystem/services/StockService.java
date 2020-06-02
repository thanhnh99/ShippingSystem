package com.shippingsystem.services;

import com.shippingsystem.models.entity.Order;
import com.shippingsystem.models.entity.Stock;
import com.shippingsystem.models.auth.ResponseStatus;
import com.shippingsystem.models.request.AddNewStockRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.repository.IStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private IStockRepository stockRepository;

    public ResponseBaseModel addNewStock(AddNewStockRequest request) {
        Stock newStock = new Stock(request.getAddress(),
                                   request.getName(),
                                   request.getStatus());

        newStock.setCreated_at(new Date());

        ResponseBaseModel response = new ResponseBaseModel();
        try {
            
            stockRepository.save(newStock);
        }
        catch (Exception e) {
            response.setStatusCode("203");
            response.getMessage().setTitle(ResponseStatus.CAN_NOT_SAVE_DATA);
        }
        response.setStatusCode("200");
        response.getMessage().setTitle(ResponseStatus.SUCCESSFULLY);
        return response;
    }

    public ResponseListModel getAll()
    {
        ResponseListModel response = new ResponseListModel();

        try{
            List<Stock> stocks = stockRepository.findAll();
            if(stocks.size()>0)
            {
                response.setStatusCode("200");
                response.getMessage().setTitle("StockStatus.SUCCESSFULLY");
                response.setData(stocks);
            }
            else
            {
                response.setStatusCode("404");
                response.getMessage().setTitle("StockStatus.STOCK_EMPTY");
                response.setData(null);
            }
        } catch (DataIntegrityViolationException e) {
            response.setStatusCode("404");
            response.getMessage().setTitle("StockStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_GET");
            return response;        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("StockStatus.CAN_NOT_GET_DATA");
        }
        return response;
    }

    public ResponseOneModel<Stock> findOneById(String stockId)
    {
        ResponseOneModel<Stock> response = new ResponseOneModel();
        try {
            Stock stock = stockRepository.findById(stockId).get();
            response.setStatusCode("200");
            response.getMessage().setTitle("StockStatus.SUCCESSFULLY");
            response.setData(stock);
        } catch (DataIntegrityViolationException e) {
            response.setStatusCode("404");
            response.getMessage().setTitle("StockStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_GET");
            response.setData(null);
        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("StockStatus.CAN_NOT_GET_DATA");
            response.setData(null);
        }

        return response;
    }

    public ResponseListModel getAllOrderInStock(Long stockId)
    {
        ResponseListModel response = new ResponseListModel();
        try{
            Order orders = stockRepository.findOrderByStockId(stockId);
//            if(orders.size()>0)
//            {
                response.setStatusCode("200");
                response.getMessage().setTitle("StockStatus.SUCCESSFULLY");
//                response.setData(orders);
//            }
//            else
//            {
//                response.setStatusCode("404");
//                response.getMessage().setTitle("StockStatus.STOCK_EMPTY");
//                response.setData(null);
//            }
        } catch (DataIntegrityViolationException e) {
            response.setStatusCode("404");
            response.getMessage().setTitle("StockStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_GET");
            return response;        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("StockStatus.CAN_NOT_GET_DATA");
        }
        return response;
    }
}
