package com.haulmont.testtask3.services.implementations;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.dao.interfaces.DoctorDao;
import com.haulmont.testtask3.models.Doctor;
import com.haulmont.testtask3.services.exceptions.ValidationException;
import com.haulmont.testtask3.services.interfaces.DoctorService;
import com.haulmont.testtask3.services.interfaces.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * Implementation of {@link DoctorService} interface.
 */
@Service
@Primary
public class DoctorServiceImpl implements DoctorService {
    private DoctorDao doctorDao;
    private RecipeService recipeService;
    private Validator validator;

    public DoctorServiceImpl() {
    }

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao, RecipeService recipeService, Validator validator) {
        this.doctorDao = doctorDao;
        this.recipeService = recipeService;
        this.validator = validator;
    }

    @Override
    public void add(Doctor entity) throws ValidationException {
        if (validator.validate(entity).isEmpty()) {
            doctorDao.add(entity);
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public void update(Doctor entity) throws ElementDoesNotExistException, ValidationException {
        if (validator.validate(entity).isEmpty() && entity.getId() != null) {
            doctorDao.update(entity);
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public Doctor getById(long id) throws ElementDoesNotExistException {
        return doctorDao.getById(id);
    }

    @Override
    public void delete(Doctor entity) throws ElementDoesNotExistException, SQLIntegrityConstraintViolationException {
        if (recipeService.getRecipeQtyByDoctorId(entity.getId()) == 0 && entity.getId() != null) {
            doctorDao.delete(entity);
        } else {
            throw new SQLIntegrityConstraintViolationException();
        }
    }

    @Override
    public List<Doctor> getAll() {
        return doctorDao.getAll();
    }
}
