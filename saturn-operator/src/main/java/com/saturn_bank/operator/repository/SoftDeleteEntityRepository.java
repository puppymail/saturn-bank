package com.saturn_bank.operator.repository;

import com.saturn_bank.operator.dao.SoftDeleteEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface SoftDeleteEntityRepository<T extends SoftDeleteEntity, ID extends Serializable> {

    List<T> findByIsDeleted(Boolean isDeleted);

}
