package com.saturn_bank.operator.repository;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    List<Account> findAccountsByUser(User user);
    Optional<Account> findAccountById(Long idAcc);

    @Query("SELECT a FROM Account a WHERE a.number = :number AND a.isDeleted = false")
    Optional<Account> findByNumber(@Param("number") String number);
}
