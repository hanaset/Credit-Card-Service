package com.hanaset.credit.web;

import com.hanaset.credit.web.rest.model.CreditResponse;
import com.hanaset.credit.web.rest.model.PaymentRequest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CreditApiRestPaymentBadRequestTest extends CreditApiRestTest{

    private PaymentRequest paymentRequest = PaymentRequest.builder()
            .cardNumber("1234567890123456")
            .validDate("0125")
            .cvc("777")
            .installment(3)
            .amount(10000)
            .vat(1000)
            .build();

    @Test
    public void creditApiRest_badRequest_cardNumber_10자리미만() throws Exception{

        paymentRequest.setCardNumber("123");
        final ResultActions resultActions = super.requestPayment(paymentRequest);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }

    @Test
    public void creditApiRest_badRequest_cardNumber_16자초과() throws Exception{

        paymentRequest.setCardNumber("1234567890123456789");
        final ResultActions resultActions = super.requestPayment(paymentRequest);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }

    @Test
    public void creditApiRest_badRequest_cardNumber_글자포함() throws Exception{

        paymentRequest.setCardNumber("123123adsfadf");
        final ResultActions resultActions = super.requestPayment(paymentRequest);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }

    @Test
    public void creditApiRest_badRequest_validDate_4글자가아닐경우() throws Exception{

        paymentRequest.setValidDate("12345");
        final ResultActions resultActions = super.requestPayment(paymentRequest);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }

    @Test
    public void creditApiRest_badRequest_validDate_숫자가아닐경우() throws Exception{

        paymentRequest.setValidDate("aasd");
        final ResultActions resultActions = super.requestPayment(paymentRequest);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }

    @Test
    public void creditApiRest_badRequest_cvc_3글자가아닐경우() throws Exception{

        paymentRequest.setCvc("1234");
        final ResultActions resultActions = super.requestPayment(paymentRequest);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }

    @Test
    public void creditApiRest_badRequest_cvc_숫자가아닐경우() throws Exception{

        paymentRequest.setCvc("csv");
        final ResultActions resultActions = super.requestPayment(paymentRequest);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }

    @Test
    public void creditApiRest_badRequest_할부_0미만() throws Exception{

        paymentRequest.setInstallment(-1);
        final ResultActions resultActions = super.requestPayment(paymentRequest);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }

    @Test
    public void creditApiRest_badRequest_할부_12초과() throws Exception{

        paymentRequest.setInstallment(13);
        final ResultActions resultActions = super.requestPayment(paymentRequest);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }

    @Test
    public void creditApiRest_badRequest_결제금액_100원미만() throws Exception{

        paymentRequest.setAmount(-1000);
        final ResultActions resultActions = super.requestPayment(paymentRequest);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }

    @Test
    public void creditApiRest_badRequest_결제금액_10억원초과() throws Exception{

        paymentRequest.setAmount(1_000_000_001);
        final ResultActions resultActions = super.requestPayment(paymentRequest);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }
}
