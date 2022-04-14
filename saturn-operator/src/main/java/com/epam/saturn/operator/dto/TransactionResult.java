package com.epam.saturn.operator.dto;

import com.epam.saturn.operator.dao.TransactionState;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TransactionResult {

    private final long id;
    private final TransactionState state;
    private final String result;

    public TransactionResult(long id, TransactionState state, String result) {
        this.id = id;
        this.state = state;
        this.result = result;
    }
}
