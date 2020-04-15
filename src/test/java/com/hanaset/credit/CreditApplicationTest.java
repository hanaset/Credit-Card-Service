package com.hanaset.credit;

import com.hanaset.credit.service.CreditCancelService;
import com.hanaset.credit.service.CreditPaymentService;
import com.hanaset.credit.service.CreditSearchService;
import com.hanaset.credit.utils.AES256Util;
import com.hanaset.credit.utils.CardInfoHelper;
import com.hanaset.credit.web.rest.CreditApiRest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
@ContextConfiguration(classes = {
        AES256Util.class,
        CardInfoHelper.class,
        CreditPaymentService.class,
        CreditCancelService.class,
        CreditSearchService.class
})
public class CreditApplicationTest {

    @Autowired
    protected CreditApiRest apiRest;

    protected String cardNumber = "1234567890123456";
    protected String validDate = "0225";
    protected String cvc = "777";


}
