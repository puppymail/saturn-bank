package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.TransactionType;
import com.epam.saturn.operator.dto.TransactionResult;

import java.math.BigDecimal;

public interface TransactionService {

    TransactionResult transfer(String accountSrcId, String accountDstId, BigDecimal amount, String purpose, TransactionType type);
}
