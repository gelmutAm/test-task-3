package com.haulmont.testtask3.services.implementations;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.dao.interfaces.RecipeDao;
import com.haulmont.testtask3.models.Recipe;
import com.haulmont.testtask3.services.exceptions.ValidationException;
import com.haulmont.testtask3.services.interfaces.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;

/**
 * Implementation of {@link RecipeService} interface.
 */
@Service
@Primary
public class RecipeServiceImpl implements RecipeService {
    private RecipeDao recipeDao;
    private Validator validator;

    public RecipeServiceImpl() {
    }

    @Autowired
    public RecipeServiceImpl(RecipeDao recipeDao, Validator validator) {
        this.recipeDao = recipeDao;
        this.validator = validator;
    }

    @Override
    public void add(Recipe entity) throws ValidationException {
        if (validator.validate(entity).isEmpty()) {
            recipeDao.add(entity);
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public void update(Recipe entity) throws ElementDoesNotExistException, ValidationException {
        if (validator.validate(entity).isEmpty() && entity.getId() != null) {
            recipeDao.update(entity);
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public Recipe getById(long id) throws ElementDoesNotExistException {
        return recipeDao.getById(id);
    }

    @Override
    public void delete(Recipe entity) throws ElementDoesNotExistException {
        if (entity.getId() != null) {
            recipeDao.delete(entity);
        } else {
            throw new ElementDoesNotExistException();
        }
    }

    @Override
    public List<Recipe> getAll() {
        return recipeDao.getAll();
    }

    @Override
    public int getRecipeQtyByPatientId(long patientId) {
        return recipeDao.getRecipeQtyByPatientId(patientId);
    }

    @Override
    public int getRecipeQtyByDoctorId(long doctorId) {
        return recipeDao.getRecipeQtyByDoctorId(doctorId);
    }

    @Override
    public List<Recipe> findAll(Long patientId, String priority, String description) {
        return recipeDao.findAll(patientId, priority, description);
    }
}
