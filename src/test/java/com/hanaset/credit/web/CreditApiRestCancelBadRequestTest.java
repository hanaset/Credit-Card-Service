package com.hanaset.credit.web;

import com.hanaset.credit.web.rest.model.CancelRequest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CreditApiRestCancelBadRequestTest extends CreditApiRestTest {

    private CancelRequest request = CancelRequest.builder()
            .id("123457890zxcvbnmasd")
            .amount(1000)
            .vat(10)
            .build();

    @Test
    public void creditApiRest_badRequest_id_20글자가아닐경우() throws Exception{

        request.setId("123adf123");
        final ResultActions resultActions = super.requestCancel(request);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }


    @Test
    public void creditApiRest_badRequest_취소금액_100원미만() throws Exception{

        request.setAmount(-200);
        final ResultActions resultActions = super.requestCancel(request);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }

    @Test
    public void creditApiRest_badRequest_취소금액_10억원초과() throws Exception{

        request.setAmount(1_000_000_001);
        final ResultActions resultActions = super.requestCancel(request);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("__REQUEST_ERROR__")))
                .andReturn();
    }
}
