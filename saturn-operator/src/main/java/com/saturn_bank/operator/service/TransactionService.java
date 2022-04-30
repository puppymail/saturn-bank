package com.saturn_bank.operator.service;

import com.saturn_bank.operator.dao.TransactionType;
import com.saturn_bank.operator.dto.TransactionResult;

import java.math.BigDecimal;

public interface TransactionService {

    TransactionResult transfer(String accountSrcId, String accountDstId, BigDecimal amount, String purpose, TransactionType type);
}
