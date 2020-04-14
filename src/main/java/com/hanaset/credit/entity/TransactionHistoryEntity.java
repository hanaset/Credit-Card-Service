package com.hanaset.credit.entity;

import com.hanaset.credit.convert.ZonedDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_history")
public class TransactionHistoryEntity {

    @Id
    @Column(length = 20)
    private String id;

    @Column(length = 450)
    private String data;

    @Column(length = 20)
    private String beforeId;

    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime completedDtime;
}
