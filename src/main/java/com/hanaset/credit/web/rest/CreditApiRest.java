package com.hanaset.credit.web.rest;

import com.hanaset.credit.web.rest.model.CancelRequest;
import com.hanaset.credit.web.rest.model.PaymentRequest;
import com.hanaset.credit.service.CreditCancelService;
import com.hanaset.credit.service.CreditPaymentService;
import com.hanaset.credit.service.CreditSearchService;
import com.hanaset.credit.web.rest.support.CreditApiRestSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/credit")
public class CreditApiRest extends CreditApiRestSupport {

    private final CreditPaymentService creditPaymentService;
    private final CreditSearchService creditSearchService;
    private final CreditCancelService creditCancelService;

    public CreditApiRest(CreditPaymentService creditPaymentService,
                         CreditSearchService creditSearchService,
                         CreditCancelService creditCancelService) {
        this.creditPaymentService = creditPaymentService;
        this.creditSearchService = creditSearchService;
        this.creditCancelService = creditCancelService;
    }

    @PostMapping(value = "/payment", produces = "application/json;charset=utf-8")
    public ResponseEntity paymentRequest(@RequestBody @Valid PaymentRequest request) {
        return ResponseEntity.ok(creditPaymentService.paymentRequest(request));
    }

    @DeleteMapping(value = "/cancel", produces = "application/json;charset=utf-8")
    public ResponseEntity cancelRequest(@RequestBody @Valid CancelRequest request) {
        return ResponseEntity.ok(creditCancelService.cancelRequest(request));
    }

    @GetMapping(value = "/search", produces = "application/json;charset=utf-8")
    public ResponseEntity search(@RequestParam String id) {
        return ResponseEntity.ok(creditSearchService.search(id));
    }
}
