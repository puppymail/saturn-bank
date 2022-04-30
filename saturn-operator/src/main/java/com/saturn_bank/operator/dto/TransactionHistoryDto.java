package com.saturn_bank.operator.dto;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransactionHistoryDto {

    private Long id;
    private String srcNumber;
    private String dstNumber;
    private BigDecimal amount;
    private String purpose;
    private LocalDateTime dateTime;
    private String state;
}
