package com.epam.saturn.operator.repository;

import com.epam.saturn.operator.dao.CardEntityDao;
import org.springframework.data.repository.CrudRepository;

public interface CardEntityRepository extends CrudRepository<CardEntityDao, Long>{

}
