package com.epam.saturn.operator.service.account.lists;

import com.epam.saturn.operator.dao.Account;

import java.util.List;

interface AccountListCommand {
    List<Account> execute(String id);
}
