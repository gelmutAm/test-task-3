package com.haulmont.testtask3.services.implementations;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.dao.interfaces.PatientDao;
import com.haulmont.testtask3.models.Patient;
import com.haulmont.testtask3.services.exceptions.ValidationException;
import com.haulmont.testtask3.services.interfaces.PatientService;
import com.haulmont.testtask3.services.interfaces.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * Implementation of {@link PatientService} interface.
 */
@Service
@Primary
public class PatientServiceImpl implements PatientService {
    private PatientDao patientDao;
    private RecipeService recipeService;
    private Validator validator;

    public PatientServiceImpl() {
    }

    @Autowired
    public PatientServiceImpl(PatientDao patientDao, RecipeService recipeService, Validator validator) {
        this.patientDao = patientDao;
        this.recipeService = recipeService;
        this.validator = validator;
    }

    @Override
    public void add(Patient entity) throws ValidationException {
        if (validator.validate(entity).isEmpty()) {
            patientDao.add(entity);
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public void update(Patient entity) throws ElementDoesNotExistException, ValidationException {
        if (validator.validate(entity).isEmpty() && entity.getId() != null) {
            patientDao.update(entity);
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public Patient getById(long id) throws ElementDoesNotExistException {
        return patientDao.getById(id);
    }

    @Override
    public void delete(Patient entity) throws ElementDoesNotExistException, SQLIntegrityConstraintViolationException {
        if (recipeService.getRecipeQtyByPatientId(entity.getId()) == 0 && entity.getId() != null) {
            patientDao.delete(entity);
        } else {
            throw new SQLIntegrityConstraintViolationException();
        }
    }

    @Override
    public List<Patient> getAll() {
        return patientDao.getAll();
    }
}
