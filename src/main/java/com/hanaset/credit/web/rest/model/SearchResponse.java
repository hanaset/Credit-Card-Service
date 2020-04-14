package com.hanaset.credit.web.rest.model;

import com.hanaset.credit.model.AmountInfo;
import com.hanaset.credit.model.CardInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    private String id;

    private CardInfo cardInfo;

    private String function;

    private AmountInfo amountInfo;

    private ZonedDateTime completedDtime;
}
