package com.saturn_bank.operator.service.account.lists;

import com.saturn_bank.operator.dao.Account;

import java.util.List;

interface AccountListCommand {
    List<Account> execute(String id);
}
