package com.saturn_bank.operator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
public class CardDto {

    private Long number;
    private Long accountNumber;
    private BigDecimal percent;
    private Integer type;
    private Integer pinCode;
    private Integer cvv2;
}
