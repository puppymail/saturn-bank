package com.epam.saturn.operator.dto;

import com.epam.saturn.operator.dao.AccountCoin;
import com.epam.saturn.operator.dao.AccountType;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountDto {
    private Long userId;
    private Boolean isDefault;
    private AccountType type;
    private AccountCoin coin;
    private BigDecimal percent;
}
