package com.shippingsystem.services;

import ch.qos.logback.classic.pattern.EnsureExceptionHandling;
import com.google.gson.Gson;
import com.mservice.allinone.models.*;
import com.mservice.allinone.processor.allinone.CaptureMoMo;
import com.mservice.allinone.processor.allinone.QueryStatusTransaction;
import com.mservice.pay.models.*;
import com.mservice.pay.processor.notallinone.AppPay;
import com.mservice.pay.processor.notallinone.POSPay;
import com.mservice.pay.processor.notallinone.TransactionQuery;
import com.mservice.pay.processor.notallinone.TransactionRefund;
import com.mservice.shared.constants.Parameter;
import com.mservice.shared.constants.RequestType;
import com.mservice.shared.sharedmodels.Environment;
import com.mservice.shared.sharedmodels.PartnerInfo;
import com.mservice.shared.utils.Encoder;
import com.shippingsystem.models.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class Payment {
    @Autowired
    OrderService orderService;
    private EnsureExceptionHandling PaymentResult;

    //TODO:Redirect to payment website => make url to redirect
    public String DisplayPaymentScreen(String order_id)
    {
        String payUrl = new String();
        try {
            Order order = orderService.findOneById(order_id).getData();

            PartnerInfo devInfo = new PartnerInfo("MOMOAY9520200529", "6spUrkmrwdt1cUPb", "M2YY71K4Crz0QMpvP6Ul9bYHJERpkSsu");
            String requestId = order.getId();
            String orderId = order.getId();
            long amount = 50000;

            String orderInfo = "Pay With MoMo";
            String returnURL = "http://localhost:8083/order/payment/response/"+ order.getId()+"/"+requestId;
            String notifyURL = "http://localhost:8083/order/payment/ipn_response/"+ order.getId();
            String extraData = "";
            String bankCode = "SML";

            Environment environment = new Environment("https://test-payment.momo.vn/gw_payment/transactionProcessor",devInfo, Environment.EnvTarget.DEV);


//      Remember to change the IDs at enviroment.properties file

//        Payment Method- Phương thức thanh toán
            CaptureMoMoResponse captureMoMoResponse = CaptureMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "");
            payUrl = captureMoMoResponse.getPayUrl();

////      Process Payment Result - Xử lý kết quả thanh toán
//            PayGateResponse payGateResponse = PaymentResult.process(environment,new PayGateResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payUrl;
    }

    //Todo: payment process
    public  ResponseEntity RequestPayment(String orderId, String requestId) throws Exception {
        PartnerInfo devInfo = new PartnerInfo("MOMOAY9520200529", "6spUrkmrwdt1cUPb", "M2YY71K4Crz0QMpvP6Ul9bYHJERpkSsu");
        Environment environment = new Environment("https://test-payment.momo.vn/gw_payment/transactionProcessor",devInfo, Environment.EnvTarget.DEV);

        //Transaction Query - Kiểm tra trạng thái giao dịch
        QueryStatusTransactionResponse queryStatusTransactionResponse = QueryStatusTransaction.process(environment, orderId, requestId);
        return (ResponseEntity) ResponseEntity.noContent();
    }

    //Todo: return payment result
    public ResponseEntity DisplayResultPayment(String orderId, String requestId)
    {
        try {
            PartnerInfo devInfo = new PartnerInfo("MOMOAY9520200529", "6spUrkmrwdt1cUPb", "M2YY71K4Crz0QMpvP6Ul9bYHJERpkSsu");
            Environment environment = new Environment("https://test-payment.momo.vn/gw_payment/transactionProcessor",devInfo, Environment.EnvTarget.DEV);

            //Transaction Query - Kiểm tra trạng thái giao dịch
            QueryStatusTransactionResponse queryStatusTransactionResponse = QueryStatusTransaction.process(environment, orderId, requestId);
            return ResponseEntity.ok(queryStatusTransactionResponse);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

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
