package com.haulmont.testtask3.dao.interfaces;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;

import java.util.List;

/**
 * Base Data Access Object interface.
 *
 * @param <T> entity type
 */
public interface BaseDao<T> {

    /**
     * Adds specified entity.
     *
     * @param entity entity to be added
     */
    void add(T entity);

    /**
     * Updates specified entity.
     *
     * @param entity entity to be updated
     * @throws ElementDoesNotExistException if specified entity does not exist
     */
    void update(T entity) throws ElementDoesNotExistException;

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
     * @throws ElementDoesNotExistException if specified entity does not exist.
     */
    void delete(T entity) throws ElementDoesNotExistException;

    /**
     * Returns all entities.
     *
     * @return all entities.
     */
    List<T> getAll();
}
