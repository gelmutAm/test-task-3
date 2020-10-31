package com.haulmont.testtask3.services.interfaces;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.services.exceptions.ValidationException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * Base entity service.
 *
 * @param <T> entity type
 */
public interface BaseService<T> {

    /**
     * Adds specified entity.
     *
     * @param entity entity to be added
     * @throws ValidationException if specified entity is no valid.
     */
    void add(T entity) throws ValidationException;

    /**
     * Updates specified entity.
     *
     * @param entity entity to be updated
     * @throws ElementDoesNotExistException if specified entity does not exist
     * @throws ValidationException          if specified entity is not valid
     */
    void update(T entity) throws ElementDoesNotExistException, ValidationException;

    /**
     * Returns entity with the specified id.
     *
     * @param id entity id
     * @return entity with the specified id.
     * @throws ElementDoesNotExistException if entity with the specified id does not exist
     */
    T getById(long id) throws ElementDoesNotExistException;

    /**
     * Deletes specified entity.
     *
     * @param entity entity to be deleted
     * @throws ElementDoesNotExistException             if specified entity does not exist
     * @throws SQLIntegrityConstraintViolationException if user is trying to delete entity
     *                                                  which is referenced from another object.
     */
    void delete(T entity) throws ElementDoesNotExistException, SQLIntegrityConstraintViolationException;

    /**
     * Returns all entities.
     *
     * @return all entities.
     */
    List<T> getAll();
}
