package com.epam.saturn.operator.dto;

import com.epam.saturn.operator.dao.TransactionState;
import com.epam.saturn.operator.dao.TransactionType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TransactionResult {

    private final long id;
    private final TransactionState state;
    private final TransactionType type;
    private final String result;

    public TransactionResult(long id, TransactionState state, TransactionType type, String result) {
        this.id = id;
        this.state = state;
        this.result = result;
        this.type = type;
    }
}
