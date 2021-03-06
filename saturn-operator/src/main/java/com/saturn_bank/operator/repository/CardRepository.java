package com.saturn_bank.operator.repository;

import com.saturn_bank.operator.dao.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, SoftDeleteEntityRepository<Card, Long> {

    @Query("SELECT c FROM Card c WHERE c.number = :number AND c.isDeleted = false")
    Optional<Card> findByNumber(@Param("number") String number);

}
