package com.shippingsystem.models.request;

import lombok.Data;

@Data
public class AddNewStockRequest {
    private String address;
    private String name;
    private int status;
}
