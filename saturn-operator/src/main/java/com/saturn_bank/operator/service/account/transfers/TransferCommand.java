package com.saturn_bank.operator.service.account.transfers;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.TransactionType;
import com.saturn_bank.operator.dto.TransactionResult;

import java.math.BigDecimal;

interface TransferCommand {
    TransactionResult execute(Account srcAccount, String dstAccount, BigDecimal amount, String purpose, TransactionType type);
}