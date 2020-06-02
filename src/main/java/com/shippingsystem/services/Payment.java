package com.shippingsystem.services;

import ch.qos.logback.classic.pattern.EnsureExceptionHandling;
import com.google.gson.Gson;
import com.mservice.allinone.models.*;
import com.mservice.allinone.processor.allinone.CaptureMoMo;
import com.mservice.allinone.processor.allinone.QueryStatusTransaction;
import com.mservice.shared.constants.Parameter;
import com.mservice.shared.sharedmodels.Environment;
import com.mservice.shared.sharedmodels.PartnerInfo;
import com.mservice.shared.utils.Encoder;
import com.shippingsystem.Enum.EOrderStatus;
import com.shippingsystem.models.entity.Order;
import com.shippingsystem.models.entity.OrderStatus;
import com.shippingsystem.repository.IOrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class Payment {
    @Autowired
    OrderService orderService;

    @Autowired
    IOrderStatusRepository orderStatusRepository;
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
            String notifyURL = "http://localhost:8083/order/payment/response";
            String extraData = "";
            String bankCode = "SML";

            Environment environment = new Environment("https://test-payment.momo.vn/gw_payment/transactionProcessor",devInfo, Environment.EnvTarget.DEV);


//      Remember to change the IDs at enviroment.properties file

//        Payment Method- Phương thức thanh toán
            CaptureMoMoResponse captureMoMoResponse = CaptureMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL,notifyURL,extraData);
            payUrl = captureMoMoResponse.getPayUrl();

////      Process Payment Result - Xử lý kết quả thanh toán
//            PayGateResponse payGateResponse = PaymentResult.process(environment,new PayGateResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payUrl;
    }

    //Todo: payment process
    public  void IPNProcess(PayGateResponse response) {
        try {
            System.out.println("Xu li IPN");
            if(response.getErrorCode()==0)
            {
                Order order = orderService.findOneById(response.getOrderId()).getData();
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setOrder(order);
                orderStatus.setValue(EOrderStatus.PAYMENT);
                orderStatusRepository.save(orderStatus);
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

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

}
