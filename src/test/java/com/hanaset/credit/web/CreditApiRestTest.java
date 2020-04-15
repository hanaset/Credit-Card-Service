package com.hanaset.credit.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaset.credit.service.CreditCancelService;
import com.hanaset.credit.service.CreditPaymentService;
import com.hanaset.credit.service.CreditSearchService;
import com.hanaset.credit.web.rest.CreditApiRest;
import com.hanaset.credit.web.rest.advice.CreditApiRestAdvice;
import com.hanaset.credit.web.rest.model.CancelRequest;
import com.hanaset.credit.web.rest.model.PaymentRequest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(MockitoJUnitRunner.class)
public class CreditApiRestTest {

    @Mock
    protected CreditCancelService creditCancelService;

    @Mock
    protected CreditSearchService creditSearchService;

    @Mock
    protected CreditPaymentService creditPaymentService;

    @InjectMocks
    protected CreditApiRest creditApiRest;

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();


    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(creditApiRest)
                .setControllerAdvice(new CreditApiRestAdvice())
                .build();
    }


    protected ResultActions requestPayment(PaymentRequest request) throws Exception {
        return mockMvc.perform(post("/credit/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print());
    }

    protected ResultActions requestCancel(CancelRequest request) throws Exception {
        return mockMvc.perform(delete("/credit/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print());
    }

    protected ResultActions search(String id) throws Exception {
        return mockMvc.perform(get("/credit/search").param("id", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
