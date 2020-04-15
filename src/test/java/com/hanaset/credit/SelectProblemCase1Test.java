package com.hanaset.credit;

import com.hanaset.credit.web.rest.exception.CreditException;
import com.hanaset.credit.web.rest.model.CancelRequest;
import com.hanaset.credit.web.rest.model.CreditResponse;
import com.hanaset.credit.web.rest.model.PaymentRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
public class SelectProblemCase1Test extends CreditApplicationTest {

    @Test
    public void 선택문제1() throws InterruptedException{

        //1. 결제 금액 11000원, 부가가치세 1000 => success
        PaymentRequest request1 = PaymentRequest.builder()
                .cardNumber(cardNumber)
                .cvc(cvc)
                .validDate(validDate)
                .installment(0)
                .amount(11000)
                .vat(1000)
                .build();

        ResponseEntity<CreditResponse> responseEntity = apiRest.paymentRequest(request1);
        // SUCCESS
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Thread.sleep(1000);

        //2. 취소 금액 1100, 부가가치세 100 => success
        CancelRequest request2 = CancelRequest.builder()
                .id(responseEntity.getBody().getId())
                .amount(1100)
                .vat(100)
                .build();

        //SUCCESS
        assertEquals(apiRest.cancelRequest(request2).getStatusCode(), HttpStatus.OK);
        Thread.sleep(1000);

        //3. 취소긍맥 3300, 부가가치세 null => success
        CancelRequest request3 = CancelRequest.builder()
                .id(responseEntity.getBody().getId())
                .amount(3300)
                .build();

        //SUCCESS
        assertEquals(apiRest.cancelRequest(request3).getStatusCode(), HttpStatus.OK);
        Thread.sleep(1000);

        try {
            //4. 취소긍맥 7000, 부가가치세 null => failed
            CancelRequest request4 = CancelRequest.builder()
                    .id(responseEntity.getBody().getId())
                    .amount(7000)
                    .build();

            apiRest.cancelRequest(request4);
            //failed
        } catch (CreditException e) {
            assertEquals(e.getHttpStatus(), HttpStatus.BAD_REQUEST);
            System.out.println("거래 실패!!!");
        }
        Thread.sleep(1000);

        try {
            //5. 취소긍맥 6600, 부가가치세 700 => failed
            CancelRequest request5 = CancelRequest.builder()
                    .id(responseEntity.getBody().getId())
                    .amount(6600)
                    .vat(700)
                    .build();

            //failed
            apiRest.cancelRequest(request5);
        } catch (CreditException e) {
            assertEquals(e.getHttpStatus(), HttpStatus.BAD_REQUEST);
            System.out.println("거래 실패!!!");
        }
        Thread.sleep(1000);

        //6. 취소긍맥 6600, 부가가치세 600 => success
        CancelRequest request6 = CancelRequest.builder()
                .id(responseEntity.getBody().getId())
                .amount(6600)
                .vat(600)
                .build();

        //SUCCESS
        assertEquals(apiRest.cancelRequest(request6).getStatusCode(), HttpStatus.OK);
        Thread.sleep(1000);

        try {
            //7. 취소긍맥 100, 부가가치세 null => failed
            CancelRequest request7 = CancelRequest.builder()
                    .id(responseEntity.getBody().getId())
                    .amount(100)
                    .build();

            //failed
            apiRest.cancelRequest(request7);
        } catch (CreditException e) {
            assertEquals(e.getHttpStatus(), HttpStatus.BAD_REQUEST);
            System.out.println("거래 실패!!!");
        }

    }
}
