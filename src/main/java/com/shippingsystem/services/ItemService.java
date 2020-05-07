package com.shippingsystem.services;

import com.shippingsystem.models.Item;
import com.shippingsystem.models.StockStatus;
import com.shippingsystem.models.requestModel.ItemRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.repository.IItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    IItemRepository itemRepository;

    public ResponseBaseModel addItem(ItemRequest request)
    {
        Item item = new Item();
        item.setName(request.getName());
        ResponseBaseModel response = new ResponseBaseModel();
        try {
            itemRepository.save(item);
        }catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("ItemStatus.CAN_NOT_SAVE_DATA");
        }
        response.setStatusCode("200");
        response.getMessage().setTitle("ItemStatus.SUCCESSFULLY");
        return response;
    }

    public ResponseOneModel<Item> getInfo(Long id)
    {
        Item item = null;
        ResponseOneModel response = new ResponseOneModel();
        try {
            item = itemRepository.findById(id).get();
        }catch (NullPointerException e)
        {
            response.setStatusCode("404");
            response.getMessage().setTitle("ItemStatus.NOT_FOUND");
            response.setData(null);
            return response;
        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("ItemStatus.CAN_NOT_GET_DATA");
            response.setData(null);
            return response;
        }
        response.setStatusCode("200");
        response.getMessage().setTitle("ItemStatus.SUCCESSFULLY");
        response.setData(item);
        return response;

    }

    public ResponseListModel<Item> getAll()
    {
        List<Item> items = null;
        ResponseListModel response = new ResponseListModel();
        try {

            items = itemRepository.findAll();
        }catch (NullPointerException e)
        {
            response.setStatusCode("404");
            response.getMessage().setTitle("ItemStatus.NOT_FOUND");
            response.setData(null);
        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("ItemStatus.CAN_NOT_GET_DATA");
            response.setData(null);
        }
        response.setStatusCode("200");
        response.getMessage().setTitle("ItemStatus.SUCCESSFULLY");
        response.setData(items);
        return response;
    }

    public ResponseBaseModel deleteItem(Long id)
    {
        ResponseBaseModel response = new ResponseBaseModel();
        try {
            itemRepository.deleteById(id);
        }
        catch (NullPointerException e)
        {
            response.setStatusCode("404");
            response.getMessage().setTitle("ItemStatus.ITEM_NOT_FOUND!_CAN_NOT_DELETE");
            return response;
        }
        catch (DataIntegrityViolationException e) {
            response.setStatusCode("404");
            response.getMessage().setTitle("ItemStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_DELETE");
            return response;        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("ItemStatus.CAN_NOT_DELETE_DATA");
        }
        response.setStatusCode("200");
        response.getMessage().setTitle("ItemStatus.SUCCESSFULLY");
        return response;
    }

}
