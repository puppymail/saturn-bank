package com.epam.saturn.operator.dto;

import com.epam.saturn.operator.dao.TransactionState;
import lombok.Getter;

@Getter
public class TransactionResult {

    private final long id;
    private final TransactionState state;

    public TransactionResult(long id, TransactionState state) {
        this.id = id;
        this.state = state;
    }
}
