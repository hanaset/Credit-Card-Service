package com.hanaset.credit.web.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotBlank(message = "카드 번호를 입력해주세요.")
    @Pattern(regexp = "[0-9]{10,16}", message = "카드 번호는 10~16자리의 숫자만 입력이 가능합니다.")
    private String cardNumber;

    @NotBlank(message = "유효날짜를 입력해주세요.")
    @Pattern(regexp = "[0-9]{4}", message = "유효날짜는 4자리의 숫자만 입력이 가능합니다.")
    private String validDate;

    @NotBlank(message = "뒷면 CVC 3자리를 입력해주세요.")
    @Pattern(regexp = "[0-9]{3}", message = "CVC는 3자라의 숫자만 입력이 가능합니다.")
    private String cvc;

    @NotNull
    @Max(value = 12, message = "할부는 최대 12개월까지입니다.")
    @Min(value = 0, message = "할부는 1~12개월까지 가능합니다.")
    private Integer installment;

    @NotNull
    @Max(value = 1000000000, message = "결제 최대 금액은 1,000,000,000원입니다.")
    @Min(value = 100, message = "결제 최소 금액은 100원입니다.")
    private Integer amount;

    private Integer vat;
}
