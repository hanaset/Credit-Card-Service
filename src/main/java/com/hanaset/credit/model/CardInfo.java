package com.hanaset.credit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardInfo {

    private String cardNumber;

    private String validDate;

    private String cvc;
}
