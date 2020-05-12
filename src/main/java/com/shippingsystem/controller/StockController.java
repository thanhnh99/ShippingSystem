package com.shippingsystem.controller;

import com.shippingsystem.models.requestModel.AddNewStockRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("")
    public ResponseEntity addNewStock(AddNewStockRequest request) {
        ResponseBaseModel response = new ResponseBaseModel();
        response = stockService.addNewStock(request);

        if(response.getStatusCode().equals("200")) return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping
    public ResponseEntity getAllStock()
    {
        ResponseListModel response = stockService.getAll();
        if(response.getStatusCode().equals("200"))
        {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(203).body(response);
    }

    @GetMapping("{stock_id}/orders")
    public  ResponseEntity getAllOrderInStock(@PathVariable(name = "stock_id") Long stockId)
    {
        ResponseListModel response = stockService.getAllOrderInStock(stockId);
        if(response.getStatusCode().equals("200"))
        {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(203).body(response);
    }

}
