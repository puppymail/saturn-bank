package com.epam.saturn.operator.service.account.transfers;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.TransactionType;
import com.epam.saturn.operator.dto.TransactionResult;

import java.math.BigDecimal;

public enum MoneyTransfer implements TransferCommand {

    BY_PHONE (TransferContextSingleton.TRANSFER_CONTEXT_SINGLETON::transferByPhone),
    BY_CARD (TransferContextSingleton.TRANSFER_CONTEXT_SINGLETON::transferByCard),
    BY_ACCOUNT (TransferContextSingleton.TRANSFER_CONTEXT_SINGLETON::transferByAccount);

    private final TransferCommand command;
    MoneyTransfer(TransferCommand command) {
        this.command = command;
    }
    @Override
    public TransactionResult execute(Account srcAccount, String dstAccount, BigDecimal amount, String purpose, TransactionType type) {
        return command.execute(srcAccount, dstAccount, amount, purpose, type);
    }
}
