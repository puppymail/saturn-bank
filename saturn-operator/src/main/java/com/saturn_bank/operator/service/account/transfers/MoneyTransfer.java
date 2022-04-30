package com.saturn_bank.operator.service.account.transfers;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.TransactionType;
import com.saturn_bank.operator.dto.TransactionResult;

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
