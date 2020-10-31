package com.haulmont.testtask3.dao.interfaces;

import com.haulmont.testtask3.models.Recipe;

import java.util.List;

/**
 * Recipe Data Access Object interface.
 */
public interface RecipeDao extends BaseDao<Recipe> {

    /**
     * Returns recipe quantity with the specified patient id.
     *
     * @param patientId patient id
     * @return recipe quantity with the specified patient id.
     */
    int getRecipeQtyByPatientId(long patientId);

    /**
     * Returns recipe quantity with the specified doctor id.
     *
     * @param doctorId doctor id
     * @return recipe quantity with the specified doctor id.
     */
    int getRecipeQtyByDoctorId(long doctorId);

    /**
     * Returns recipes {@code List} filtered by specified properties.
     *
     * @param patientId   patient filter
     * @param priority    priority filter
     * @param description description filter
     * @return recipes {@code List} filtered by specified properties.
     */
    List<Recipe> findAll(Long patientId, String priority, String description);
}
