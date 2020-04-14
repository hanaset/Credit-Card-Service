package com.hanaset.credit.web.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditResponse {

    private String id;

    private Integer amount;

    private Integer vat;

    private ZonedDateTime completedDtime;

}
