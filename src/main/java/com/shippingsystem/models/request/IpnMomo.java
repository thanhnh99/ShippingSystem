package com.shippingsystem.models.request;

import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpnMomo {
    private String partnerCode;
    private  String accessKey;
    private String requestId;
    private String orderId;
    private long amount;
    private String orderInfo;
    private String orderType;
    private String transId;
    private int errorCode;
    private String message;
    private String localMessage;
    private String payType;
    private String responseTime;
    private String extraData;
    private String signature;
}
