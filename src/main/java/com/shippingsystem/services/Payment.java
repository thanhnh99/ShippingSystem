package com.shippingsystem.services;

import com.google.gson.Gson;
import com.mservice.pay.models.POSPayResponse;
import com.mservice.pay.models.TransactionQueryResponse;
import com.mservice.pay.models.TransactionRefundResponse;
import com.mservice.pay.processor.notallinone.POSPay;
import com.mservice.pay.processor.notallinone.TransactionQuery;
import com.mservice.pay.processor.notallinone.TransactionRefund;
import com.mservice.shared.constants.Parameter;
import com.mservice.shared.constants.RequestType;
import com.mservice.shared.sharedmodels.Environment;
import com.mservice.shared.sharedmodels.PartnerInfo;
import com.mservice.shared.utils.Encoder;
import com.shippingsystem.repository.IOrderRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Data
@Service
public class Payment {
    @Autowired
    private IOrderRepository orderRepository;

    //TODO:Redirect to payment website => make url to redirect
    public String DisplayPaymentScreen(String order_id){
        return "Day la link redirect sang trang thanh toan. Doi lam xong thi lamf tiep";
    }

    //Todo: payment process
    public  ResponseEntity RequestPayment(String orderId)
    {

        return (ResponseEntity) ResponseEntity.noContent();
    }

    //Todo: return payment result
    public ResponseEntity DisplayResultPayment()
    {
        return (ResponseEntity) ResponseEntity.noContent();
    }


    public static String generateRSA(PartnerInfo partnerInfo,
                                     String orderId,
                                     String orderInfo,
                                     Long amount,
                                     String requestId,
                                     String returnUrl,
                                     String notifyUrl,
                                     String extraData,
                                     String requestType) throws Exception {
        // current version of Parameter key name is 2.0
        Map<String, Object> rawData = new HashMap<String, Object>();
        rawData.put(Parameter.ACCESS_KEY, partnerInfo.getAccessKey());
        rawData.put(Parameter.AMOUNT, amount);
        rawData.put(Parameter.EXTRA_DATA, extraData);
        rawData.put(Parameter.PARTNER_CODE, partnerInfo.getPartnerCode());
        rawData.put(Parameter.NOTIFY_URL, notifyUrl);
        rawData.put(Parameter.ORDER_ID, orderId);
        rawData.put(Parameter.REQUEST_ID, requestId);
        rawData.put(Parameter.REQUEST_TYPE, requestType);
        rawData.put(Parameter.ORDER_INFO, orderInfo);
        rawData.put(Parameter.RETURN_URL,returnUrl);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(rawData);
        byte[] testByte = jsonStr.getBytes(StandardCharsets.UTF_8);
        String hashRSA = Encoder.encryptRSA(testByte, partnerInfo.getSecretKey());

        return hashRSA;
    }
}
