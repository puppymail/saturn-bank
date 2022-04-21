package com.epam.saturn.operator.service.account;

import com.epam.saturn.operator.dao.Transaction;
import com.epam.saturn.operator.dao.TransactionState;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TransactionResult {
    private TransactionState state;
    private String log;

    public TransactionResult(TransactionState state, String log) {
        this.state = state;
        this.log = log;
    }
}
