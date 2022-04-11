package com.epam.saturn.operator.service.account;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.AccountType;
import com.epam.saturn.operator.dao.AccountCoin;
import com.epam.saturn.operator.dao.Transaction;
import com.epam.saturn.operator.service.account.lists.AccountList;
import com.epam.saturn.operator.service.account.transfers.MoneyTransfer;


import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Account openAccount(User user, AccountType type, AccountCoin coin, BigDecimal percent);
    void closeAccount(Account account);
    TransactionResult depositMoney(Account account, BigDecimal amount);
    TransactionResult withdrawMoney(Account account, BigDecimal amount);
    List<Transaction> getAccountTransactionHistory(Account account);
    List<Account> getAccounts(User user);

    default List<Account> getAccounts(String id, String idType) {
        for (AccountList list : AccountList.values()) {
            if (list == AccountList.valueOf(idType)){
                return list.execute(id);
            }
        }
        throw new IllegalArgumentException("identifier type isn't present in the enum");
    }

    default TransactionResult transfer(Account srcAccount, String dstAccount, BigDecimal amount, String idType) {
        for (MoneyTransfer type : MoneyTransfer.values()) {
            if (type == MoneyTransfer.valueOf(idType)){
                return type.execute(srcAccount, dstAccount, amount);
            }
        }
        throw new IllegalArgumentException("transfer type isn't present in the enum");
    }
}
