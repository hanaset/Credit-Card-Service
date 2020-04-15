package com.hanaset.credit.service;

import com.hanaset.credit.repository.TransactionHistoryRepository;
import com.hanaset.credit.utils.CardInfoHelper;
import com.hanaset.credit.web.rest.model.CreditResponse;
import com.hanaset.credit.web.rest.model.PaymentRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CreditPaymentServiceTest {

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private CardInfoHelper cardInfoHelper;

    @InjectMocks
    private CreditPaymentService creditPaymentService;

    private PaymentRequest request;
    private String enc;
    @Before
    public void setUp() {

        request = PaymentRequest.builder()
                .cardNumber("123123123123")
                .validDate("0125")
                .cvc("777")
                .installment(3)
                .amount(10000)
                .vat(1000)
                .build();

        enc = "sYMbiD84JAnrzyo0nz58GSgocTYUvcwUJm3MzNmCNXc=";

    }

    @Test
    public void paymentTest() {

        given(cardInfoHelper.encrypt(any())).willReturn(enc);

        CreditResponse response = creditPaymentService.paymentRequest(request);

        assertEquals(response.getAmount(), request.getAmount());
        assertEquals(response.getVat(), response.getVat());
        assertNotNull(response.getId());
        assertNotNull(response.getCompletedDtime());
    }
}
