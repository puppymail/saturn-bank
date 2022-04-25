package com.epam.saturn.operator.service.account;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.TransactionType;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.AccountType;
import com.epam.saturn.operator.dao.AccountCoin;
import com.epam.saturn.operator.dao.Transaction;
import com.epam.saturn.operator.dto.AccountDto;
import com.epam.saturn.operator.dto.TransactionResult;
import com.epam.saturn.operator.service.account.lists.AccountList;
import com.epam.saturn.operator.service.account.transfers.MoneyTransfer;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    Account openAccount(AccountDto accountDto);
    void closeAccount(Account account);
    void setAccountDefault(Long id);

    TransactionResult depositMoney(Account account, BigDecimal amount);
    TransactionResult withdrawMoney(Account account, BigDecimal amount);
    List<Transaction> getAccountTransactionHistory(Account account);

    List<Account> getAllUserAccounts(User user);
    List<Account> getActiveUserAccounts(User user);
    Optional<Account> getAccount(Long id);
    List<Account> findAll();

    default List<Account> getAllAccounts(String id, String idType) {
        for (AccountList list : AccountList.values()) {
            if (list == AccountList.valueOf(idType)){
                return list.execute(id);
            }
        }
        throw new IllegalArgumentException("identifier type isn't present in the enum");
    }

    default TransactionResult transfer(Account srcAccount, String dstAccount, BigDecimal amount, String purpose, TransactionType transactionType, String idType) {
        for (MoneyTransfer type : MoneyTransfer.values()) {
            if (type == MoneyTransfer.valueOf(idType)){
                return type.execute(srcAccount, dstAccount, amount, purpose, transactionType);
            }
        }
        throw new IllegalArgumentException("transfer type isn't present in the enum");
    }
}
