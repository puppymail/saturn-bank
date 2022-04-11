package com.epam.saturn.operator.service.account;

import com.epam.saturn.operator.dao.Transaction;
import com.epam.saturn.operator.dao.TransactionState;

public class TransactionResult {
    Transaction transaction;
    TransactionState state;
    String log;
}
