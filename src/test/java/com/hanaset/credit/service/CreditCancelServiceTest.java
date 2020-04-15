package com.hanaset.credit.service;

import com.hanaset.credit.entity.TransactionHistoryEntity;
import com.hanaset.credit.repository.TransactionHistoryRepository;
import com.hanaset.credit.utils.CardInfoHelper;
import com.hanaset.credit.web.rest.exception.CreditException;
import com.hanaset.credit.web.rest.model.CancelRequest;
import com.hanaset.credit.web.rest.model.CreditResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CreditCancelServiceTest {

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private CardInfoHelper cardInfoHelper;

    @InjectMocks
    private CreditCancelService creditCancelService;

    private TransactionHistoryEntity paymentEntity;

    private List<TransactionHistoryEntity> cancelEntityList;

    @Before
    public void setUp() {
        String data = "_446PAYMENT___XXXXXXXXXXXXXXXXXXXX1234567890123456____001125777____1100000000010000" +
                "____________________YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "____________________________________________________________________________________________________" +
                "_______________________________________________________________________" +
                "____________________________________________________________________________";

        paymentEntity = TransactionHistoryEntity.builder()
                .id("test_id")
                .data(data)
                .beforeId("")
                .completedDtime(ZonedDateTime.now())
                .build();
    }

    @Test
    public void cancelTest() {

        CancelRequest request = CancelRequest.builder()
                .id("test_id")
                .amount(1000)
                .vat(10)
                .build();

        given(transactionHistoryRepository.findById(any())).willReturn(Optional.of(paymentEntity));
        given(transactionHistoryRepository.findByBeforeId(anyString())).willReturn(Optional.ofNullable(cancelEntityList));

        CreditResponse response = creditCancelService.cancelRequest(request);

        assertEquals(response.getAmount(), request.getAmount());
        assertEquals(response.getVat(), response.getVat());
        assertNotNull(response.getId());
        assertNotNull(response.getCompletedDtime());
    }

    @Test(expected = CreditException.class)
    public void overCancelTest() {

        CancelRequest request = CancelRequest.builder()
                .id("test_id")
                .amount(10000000)
                .vat(10)
                .build();

        given(transactionHistoryRepository.findById(any())).willReturn(Optional.of(paymentEntity));
        given(transactionHistoryRepository.findByBeforeId(anyString())).willReturn(Optional.ofNullable(cancelEntityList));

        creditCancelService.cancelRequest(request);
    }
}
