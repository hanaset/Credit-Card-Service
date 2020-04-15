package com.hanaset.credit.web;

import com.hanaset.credit.web.rest.CreditApiRest;
import com.hanaset.credit.web.rest.model.CancelRequest;
import com.hanaset.credit.web.rest.model.CreditResponse;
import com.hanaset.credit.web.rest.model.PaymentRequest;
import com.hanaset.credit.web.rest.model.SearchResponse;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreditApiRestSuccessTest extends CreditApiRestTest {

    @Test
    public void creditApiRest_payment_success_test() throws Exception {

        PaymentRequest request = PaymentRequest.builder()
                .cardNumber("123123123123")
                .validDate("0125")
                .cvc("777")
                .installment(3)
                .amount(10000)
                .vat(1000)
                .build();

        given(super.creditPaymentService.paymentRequest(any())).willReturn(new CreditResponse());

        final ResultActions resultActions = super.requestPayment(request);

        resultActions
                .andExpect(status().isOk());
    }

    @Test
    public void creditApiRest_search_success_test() throws Exception {

        given(super.creditSearchService.search(any())).willReturn(new SearchResponse());

        final ResultActions resultActions = super.search("test_id");

        resultActions
                .andExpect(status().isOk());

    }

    @Test
    public void creditApiRest_cancel_success_test() throws Exception {

        given(super.creditCancelService.cancelRequest(any())).willReturn(new CreditResponse());

        CancelRequest request = CancelRequest.builder()
                .id("12345678901234567890")
                .amount(1000)
                .vat(10)
                .build();

        final ResultActions resultActions = super.requestCancel(request);

        resultActions
                .andExpect(status().isOk());
    }
}
