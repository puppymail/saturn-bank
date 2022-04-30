package com.saturn_bank.operator.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;
import static org.springframework.util.StringUtils.capitalize;

import com.saturn_bank.operator.dao.SoftDeleteEntity;
import com.saturn_bank.operator.exception.DeletedEntityException;
import com.saturn_bank.operator.exception.NoSuchEntityException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
public class Utils {

    public static <E extends SoftDeleteEntity> E softDeleteEntityValidityCheck(Optional<E> entityOpt, Class<E> entityClass) {
        if (entityOpt.isEmpty()) {
            log.error("!No matching " + entityClass.getSimpleName() + " entity found!");
            throw new NoSuchEntityException("No matching " + entityClass.getSimpleName() + " entity found");
        }
        E entity = entityOpt.get();
        if (entity.isDeleted()) {
            log.error("!" + entityClass.getSimpleName() + " entity provided is deleted!");
            throw new DeletedEntityException(entityClass.getSimpleName() + " entity provided is deleted");
        }

        return entity;
    }

    protected static <E> void updateField(E updatedEntity, E existingEntity, String fieldName) {
        Method getMethod = findMethod(existingEntity.getClass(), "get" + capitalize(fieldName));
        if (isNull(getMethod)) {
            log.error("!No getter exists for field " + fieldName +
                      " in " + existingEntity.getClass().getSimpleName() + " entity!");
            return;
        }
        Method setMethod = findMethod(existingEntity.getClass(), "set" + capitalize(fieldName), getMethod.getReturnType());
        if (isNull(setMethod)) {
            log.error("!No setter exists for field " + fieldName +
                      " in " + existingEntity.getClass().getSimpleName() + " entity!");
            return;
        }
        if (nonNull(invokeMethod(getMethod, updatedEntity))) {
            invokeMethod(setMethod, existingEntity, invokeMethod(getMethod, updatedEntity));
            log.info("Field " + fieldName + " in " + existingEntity.getClass().getSimpleName() + " updated");
        }
    }

}
