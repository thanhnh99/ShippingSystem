package com.shippingsystem.controller;

import com.shippingsystem.models.request.ItemRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @GetMapping("")
    public ResponseEntity getAllItem()
    {
        ResponseListModel response = new ResponseListModel();
        response = itemService.getAll();

        if(response.getStatusCode().equals("200")) return ResponseEntity.ok().body(response);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    @PostMapping("")
    public  ResponseEntity addItem(@RequestBody ItemRequest itemRequest)
    {
        ResponseBaseModel response = new ResponseBaseModel();
        response = itemService.addItem(itemRequest);

        if(response.getStatusCode().equals("200")) return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }


    @DeleteMapping("{item_id}")
    public ResponseEntity deleteItem(@PathVariable(name = "item_id") String itemId)
    {
        ResponseBaseModel response = new ResponseBaseModel();
        response = itemService.deleteItem(itemId);
        if(response.getStatusCode().equals("200")) return ResponseEntity.ok().body(response);
        return ResponseEntity.status(203).body(response);
    }
}
