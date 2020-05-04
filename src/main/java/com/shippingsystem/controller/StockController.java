package com.shippingsystem.controller;

import com.shippingsystem.models.StockStatus;
import com.shippingsystem.models.requestModel.AddNewStockRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("/add")
    public ResponseEntity addNewStock(AddNewStockRequest request) {
        ResponseBaseModel response = new ResponseBaseModel();
        response = stockService.addNewStock(request);

        if(response.getStatusCode().equals("200")) return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
