package com.haulmont.testtask3.services.interfaces;

import com.haulmont.testtask3.models.Recipe;

import java.util.List;

/**
 * Recipe Service interface.
 */
public interface RecipeService extends BaseService<Recipe> {

    /**
     * Returns quantity of recipes with specified patient id.
     *
     * @param patientId patient id
     * @return quantity of recipes with specified patient id.
     */
    int getRecipeQtyByPatientId(long patientId);

    /**
     * Returns quantity of recipes with specified doctor id.
     *
     * @param doctorId doctor id
     * @return quantity of recipes with specified doctor id.
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
