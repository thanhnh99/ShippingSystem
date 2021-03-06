package com.shippingsystem.models.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String name;
    private String receiveName;
    private String receiveAddress;
    private String receivePhone;
    private String sendName;
    private String sendAddress;
    private String itemCode;
}
