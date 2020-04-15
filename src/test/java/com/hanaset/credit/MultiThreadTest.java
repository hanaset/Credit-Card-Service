package com.hanaset.credit;

import com.hanaset.credit.web.rest.CreditApiRest;
import com.hanaset.credit.web.rest.model.CancelRequest;
import com.hanaset.credit.web.rest.model.CreditResponse;
import com.hanaset.credit.web.rest.model.PaymentRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
public class MultiThreadTest extends CreditApplicationTest{

    private PaymentRequest paymentRequest = PaymentRequest.builder()
            .cardNumber(cardNumber)
            .cvc(cvc)
            .validDate(validDate)
            .installment(0)
            .amount(11000)
            .vat(1000)
            .build();

    @Test
    public void payment_multiThread_test() throws Exception{

        for(int i = 0 ; i < 10 ; i ++) {
            Thread t = new Thread(() -> {
                System.out.println(apiRest.paymentRequest(paymentRequest));
            });

            t.start();
            Thread.sleep(100);
        }

        Thread.sleep(5000);
    }

    @Test
    public void pullCancel_multiThread_test() throws Exception {

        ResponseEntity<CreditResponse> response = apiRest.paymentRequest(paymentRequest);
        Thread.sleep(1000);

        CancelRequest cancelRequest = CancelRequest.builder()
                .id(response.getBody().getId())
                .amount(response.getBody().getAmount())
                .vat(response.getBody().getVat())
                .build();

        for(int i = 0 ; i < 10 ; i ++) {
            Thread t = new Thread(() -> {
                System.out.println(apiRest.cancelRequest(cancelRequest));
            });

            t.start();
            Thread.sleep(100);
        }

        Thread.sleep(5000);

    }

    @Test
    public void partCancel_multiThread_test() throws Exception {

        ResponseEntity<CreditResponse> response = apiRest.paymentRequest(paymentRequest);
        Thread.sleep(1000);

        CancelRequest cancelRequest = CancelRequest.builder()
                .id(response.getBody().getId())
                .amount(response.getBody().getAmount() / 10)
                .vat(response.getBody().getVat()/ 10)
                .build();

        for(int i = 0 ; i < 10 ; i ++) {
            Thread t = new Thread(() -> {
                System.out.println(apiRest.cancelRequest(cancelRequest));
            });

            t.start();
//            Thread.sleep(100);
        }

        Thread.sleep(5000);

    }
}
