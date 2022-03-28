package com.epam.saturn.operator.repository;

import com.epam.saturn.operator.dao.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {

}
