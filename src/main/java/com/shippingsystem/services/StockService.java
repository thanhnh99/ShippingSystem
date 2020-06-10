package com.shippingsystem.services;

import com.shippingsystem.models.entity.Order;
import com.shippingsystem.models.entity.OrderStatus;
import com.shippingsystem.models.entity.Stock;
import com.shippingsystem.models.auth.ResponseStatus;
import com.shippingsystem.models.entity.User;
import com.shippingsystem.models.request.AddNewStockRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.repository.IStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private IStockRepository stockRepository;
    @Autowired
    private UserService userService;

    public ResponseBaseModel addNewStock(AddNewStockRequest request) {
        Stock newStock = new Stock(request.getName(),
                                   request.getAddress(),
                                   true,
                                   request.getAcreage(),
                                   0,
                                   null);

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

    public ResponseListModel getAllOrderInStock(String  stockId)
    {
        ResponseListModel response = new ResponseListModel();
        try{
            Order orders = stockRepository.findOrderByStockId(stockId);
                response.setStatusCode("200");
                response.getMessage().setTitle("StockStatus.SUCCESSFULLY");

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
    public ResponseBaseModel deleteStock(String stockId)
    {
        ResponseBaseModel response = new ResponseBaseModel();
        try {
            stockRepository.deleteById(stockId);
            response.setStatusCode("200");
            response.getMessage().setTitle("StockStatus.SUCCESSFULLY");
        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("StockStatus.CAN_NOT_DELETE_DATA");
        }
        return response;
    }
    public  ResponseOneModel editStockInfo(AddNewStockRequest request, String stockId, HttpServletRequest authen)
    {
        ResponseOneModel response = new ResponseOneModel();
        final String requestTokenHeader = authen.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        String email =null;
        try {
            email = userService.getEmailFromToken(jwtToken);
            User user = userService.findUserByEmail(email);
            Stock stock = stockRepository.getOne(stockId);
            stock.setAcreage(request.getAcreage());
            stock.setAddress(request.getAddress());
            stock.setName(request.getName());
            stock.setUpdated_at(new Date());
            stock.setUpdated_by(user.getId());
            stockRepository.save(stock);

            response.setStatusCode("200");
            response.getMessage().setTitle("StockStatus.SUCCESSFULLY");
        }catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("StockStatus.CAN_NOT_EDIT_DATA");
        }
        return response;
    }
}
