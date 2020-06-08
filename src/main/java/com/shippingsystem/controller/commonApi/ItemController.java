package com.shippingsystem.controller.commonApi;

import com.shippingsystem.models.request.ItemRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("common/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @GetMapping("")
    public ResponseEntity getAllItem()
    {
        ResponseListModel response = new ResponseListModel();
        response = itemService.getAll();
        http://localhost:5000/order
        if(response.getStatusCode().equals("200")) return ResponseEntity.ok().body(response);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }
}
