package com.shippingsystem.services;

import com.shippingsystem.models.Item;
import com.shippingsystem.models.requestModel.ItemRequest;
import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.models.response.ResponseListModel;
import com.shippingsystem.models.response.ResponseOneModel;
import com.shippingsystem.repository.IItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ItemService {
    @Autowired
    IItemRepository itemRepository;

    public ResponseBaseModel addItem(ItemRequest request)
    {
        ResponseBaseModel response = new ResponseBaseModel();
        try
        {
            Item item = itemRepository.findByCode(request.getCode());
            if(item==null)
            {
                Item newItem = new Item();
                newItem.setName(request.getName());
                newItem.setCode(request.getCode());
                newItem.setMultiplicity(request.getMultiplicity());
                try {
                    itemRepository.save(newItem);
                }
                catch (DataIntegrityViolationException e) {
                    response.setStatusCode("404");
                    response.getMessage().setTitle("ItemStatus.FOREIGN_KEY_CONSTRAINT_FAILS!_CAN_NOT_SAVE");
                    return response;
                }
                catch (Exception e)
                {
                    response.setStatusCode("203");
                    response.getMessage().setTitle("ItemStatus.CAN_NOT_SAVE_DATA");
                }
            }
            else
            {
                response.setStatusCode("405");
                response.getMessage().setTitle("ItemStatus.ITEM_EXISTED");
                return response;
            }
        }
        catch (Exception e)
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
        ResponseOneModel response = new ResponseOneModel();
        try {
            Item item = itemRepository.findById(id).get();
            response.setStatusCode("200");
            response.getMessage().setTitle("ItemStatus.SUCCESSFULLY");
            response.setData(item);
        }catch (EntityNotFoundException e)
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
        return response;

    }

    public ResponseListModel<Item> getAll()
    {
        ResponseListModel response = new ResponseListModel();
        try {

            List<Item> items = itemRepository.findAll();
            if(items.size()==0)
            {
                response.setStatusCode("404");
                response.getMessage().setTitle("ItemStatus.ITEM_NOT_FOUND");
                response.setData(null);
                return response;
            }
            response.setStatusCode("200");
            response.getMessage().setTitle("ItemStatus.SUCCESSFULLY");
            response.setData(items);
        }
        catch (Exception e)
        {
            response.setStatusCode("203");
            response.getMessage().setTitle("ItemStatus.CAN_NOT_GET_DATA");
            response.setData(null);
        }
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
