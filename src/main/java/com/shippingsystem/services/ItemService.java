package com.shippingsystem.services;

import com.shippingsystem.models.Item;
import com.shippingsystem.repository.IItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    IItemRepository itemRepository;

    public Optional<Item> getInfo(Long id)
    {
        return itemRepository.findById(id);
    }
}
