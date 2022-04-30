package com.saturn_bank.operator.service.account.lists;

import com.saturn_bank.operator.dao.Account;
import java.util.List;

public enum AccountList implements AccountListCommand{

    BY_PHONE(AccountListerContextSingleton.ACCOUNT_LISTER_CONTEXT_SINGLETON::showListByPhone),
    BY_CARD(AccountListerContextSingleton.ACCOUNT_LISTER_CONTEXT_SINGLETON::showListByCard);

    private final AccountListCommand command;
    AccountList(AccountListCommand command) {
        this.command = command;
    }

    @Override
    public List<Account> execute(String id) {
        return command.execute(id);
    }
}
