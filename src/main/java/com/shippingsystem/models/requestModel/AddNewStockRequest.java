package com.shippingsystem.models.requestModel;

import lombok.Data;

@Data
public class AddNewStockRequest {
    private String address;
    private String name;
    private int status;
}
