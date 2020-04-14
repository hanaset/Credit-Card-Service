package com.hanaset.credit.web.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelRequest {

    @NotBlank
    @Length(min = 20, max = 20, message = "관리번호는 20글자입니다.")
    private String id;

    @NotNull
    @Max(value = 1000000000, message = "최대 취소 금액은 1,000,000,000원입니다.")
    @Min(value = 100, message = "최소 취소 금액은 100원입니다.")
    private Integer amount;

    private Integer vat;
}
