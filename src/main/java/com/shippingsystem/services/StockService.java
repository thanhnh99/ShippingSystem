package com.shippingsystem.services;

import com.shippingsystem.models.StockStatus;
import com.shippingsystem.models.Warehouse;
import com.shippingsystem.models.requestModel.AddNewStockRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.repository.IStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StockService {

    @Autowired
    private IStockRepository stockRepository;

    public ResponseBaseModel addNewStock(AddNewStockRequest request) {
        Warehouse newStock = new Warehouse(request.getAddress(),
                                   request.getName(),
                                   request.getStatus());

        newStock.setCreated_at(new Date());

        ResponseBaseModel response = new ResponseBaseModel();
        try {
            
            stockRepository.save(newStock);
        }
        catch (Exception e) {
            response.setStatusCode("203");
            response.getBody().setTitle(StockStatus.CAN_NOT_SAVE_DATA);
        }
        response.setStatusCode("200");
        response.getBody().setTitle(StockStatus.SUCCESSFULLY);
        return response;
    }
}
