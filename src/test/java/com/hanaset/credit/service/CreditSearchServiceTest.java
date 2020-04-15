package com.hanaset.credit.service;

import com.hanaset.credit.entity.TransactionHistoryEntity;
import com.hanaset.credit.model.CardInfo;
import com.hanaset.credit.repository.TransactionHistoryRepository;
import com.hanaset.credit.utils.CardInfoHelper;
import com.hanaset.credit.web.rest.model.SearchResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CreditSearchServiceTest {

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private CardInfoHelper cardInfoHelper;

    @InjectMocks
    private CreditSearchService creditSearchService;

    private TransactionHistoryEntity paymentEntity;

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
    public void searchTest() {

        CardInfo cardInfo = CardInfo.builder()
                .cardNumber("1234567890123456")
                .cvc("777")
                .validDate("1125")
                .build();

        given(transactionHistoryRepository.findById(any())).willReturn(Optional.of(paymentEntity));
        given(cardInfoHelper.decrypt(anyString())).willReturn(cardInfo);

        SearchResponse response = creditSearchService.search("test_id");

        assertEquals(cardInfo, response.getCardInfo());
    }
}
