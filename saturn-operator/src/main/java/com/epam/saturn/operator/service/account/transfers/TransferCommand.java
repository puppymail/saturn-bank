package com.epam.saturn.operator.service.account.transfers;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.TransactionType;
import com.epam.saturn.operator.service.account.TransactionResult;

import java.math.BigDecimal;

interface TransferCommand {
    TransactionResult execute(Account srcAccount, String dstAccount, BigDecimal amount, String purpose, TransactionType type);
}