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
public class SelectProblemCase3Test extends CreditApplicationTest {

    @Test
    public void 선택문제3() throws InterruptedException{

        //1. 결제 금액 20000원, 부가가치세 null => success
        PaymentRequest request1 = PaymentRequest.builder()
                .cardNumber(cardNumber)
                .cvc(cvc)
                .validDate(validDate)
                .installment(0)
                .amount(20000)
                .build();

        ResponseEntity<CreditResponse> responseEntity = apiRest.paymentRequest(request1);
        // SUCCESS
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Thread.sleep(1000);

        //2. 취소 금액 10000, 부가가치세 1000 => success
        CancelRequest request2 = CancelRequest.builder()
                .id(responseEntity.getBody().getId())
                .amount(10000)
                .vat(1000)
                .build();

        //SUCCESS
        assertEquals(apiRest.cancelRequest(request2).getStatusCode(), HttpStatus.OK);
        Thread.sleep(1000);

        try {
            //3. 취소긍맥 10000, 부가가치세 909 => failed
            CancelRequest request3 = CancelRequest.builder()
                    .id(responseEntity.getBody().getId())
                    .amount(10000)
                    .vat(0)
                    .build();

            apiRest.cancelRequest(request3);
            //failed
        } catch (CreditException e) {
            assertEquals(e.getHttpStatus(), HttpStatus.BAD_REQUEST);
            System.out.println("거래 실패!!!");
        }
        Thread.sleep(1000);

        //4. 취소긍맥 10000, 부가가치세 null => success
        CancelRequest request6 = CancelRequest.builder()
                .id(responseEntity.getBody().getId())
                .amount(10000)
                .build();

        //SUCCESS
        assertEquals(apiRest.cancelRequest(request6).getStatusCode(), HttpStatus.OK);

    }
}
