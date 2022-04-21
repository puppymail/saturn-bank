package com.epam.saturn.operator.repository;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findByUser(User user);

    @Query(value = "SELECT * FROM saturn_bank.account", nativeQuery = true)
    List<Account> findAllAccounts(User user);
}
