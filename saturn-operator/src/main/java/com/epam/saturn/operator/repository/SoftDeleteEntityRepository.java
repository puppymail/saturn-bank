package com.epam.saturn.operator.repository;

import com.epam.saturn.operator.dao.SoftDeleteEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface SoftDeleteEntityRepository<T extends SoftDeleteEntity, ID extends Serializable> {

    List<T> findByIsDeleted(Boolean isDeleted);

}
