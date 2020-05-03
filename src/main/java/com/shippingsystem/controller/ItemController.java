package com.shippingsystem.controller;

import com.shippingsystem.models.Item;
import com.shippingsystem.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @GetMapping("")
    public List<Item> getAllItem()
    {
        return itemService.getAll();
    }
}
