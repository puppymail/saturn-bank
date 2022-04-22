package com.epam.saturn.operator.repository;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findByUser(User user);

    @Query(value = "SELECT * FROM saturn_bank.account WHERE saturn_bank.account.user_id = :userId", nativeQuery = true)
    List<Account> findAllAccounts(Long userId);

    default List<Account> findAllAccounts(User user) {
        return findAllAccounts(user.getId());
    }

    @Query("SELECT a FROM Account a WHERE a.number = :number AND a.isDeleted = false")
    Optional<Account> findByNumber(@Param("number") String number);
}
