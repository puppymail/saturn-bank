package com.epam.saturn.operator.service.account;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.TransactionType;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dto.AccountDto;
import com.epam.saturn.operator.dto.TimeRange;
import com.epam.saturn.operator.dto.TransactionHistoryDto;
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

    List<TransactionHistoryDto> getAccountTransactionsHistory(Account account, Boolean full);
    List<TransactionHistoryDto> getAccountTransactionsHistoryForTimeRange(Account account, TimeRange range);

    List<Account> getAllUserAccounts(User user);
    List<Account> getActiveUserAccounts(User user);
    Optional<Account> findAccountByAccountId(Long idAcc);

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
