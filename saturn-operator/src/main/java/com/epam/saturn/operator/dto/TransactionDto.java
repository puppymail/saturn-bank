package com.epam.saturn.operator.dto;

import com.epam.saturn.operator.dao.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
public class TransactionDto {

    private String accountSrc;
    private String accountDst;
    private BigDecimal amount;
    private String purpose;
    private TransactionType type;
}