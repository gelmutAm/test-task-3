package com.haulmont.testtask3.services.implementations;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.dao.interfaces.DoctorDao;
import com.haulmont.testtask3.models.Doctor;
import com.haulmont.testtask3.services.exceptions.ValidationException;
import com.haulmont.testtask3.services.interfaces.RecipeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DoctorServiceImplTest {

    @Mock
    private DoctorDao doctorDao;

    @Mock
    private RecipeService recipeService;

    @Mock
    private Validator validator;

    @InjectMocks
    private DoctorServiceImpl testDoctorServiceImpl;

    @Test
    public void addValidDoctorTest() throws ValidationException {
        Doctor doctor = new Doctor();
        doNothing().when(doctorDao).add(doctor);
        testDoctorServiceImpl.add(doctor);
    }

    @Test(expected = ValidationException.class)
    public void addInvalidDoctorTest() throws ValidationException {
        Doctor doctor = new Doctor();
        Set<ConstraintViolation<Doctor>> set = new HashSet<>();
        set.add(null);
        when(validator.validate(doctor)).thenReturn(set);
        testDoctorServiceImpl.add(doctor);
    }

    @Test
    public void updateValidDoctorTest() throws ElementDoesNotExistException, ValidationException {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doNothing().when(doctorDao).update(doctor);
        testDoctorServiceImpl.update(doctor);
    }

    @Test(expected = ValidationException.class)
    public void updateInvalidDoctorTest() throws ValidationException, ElementDoesNotExistException {
        Doctor doctor = new Doctor();
        Set<ConstraintViolation<Doctor>> set = new HashSet<>();
        set.add(null);
        when(validator.validate(doctor)).thenReturn(set);
        testDoctorServiceImpl.update(doctor);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void updateNotExistingDoctorTest() throws ElementDoesNotExistException, ValidationException {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doThrow(ElementDoesNotExistException.class).when(doctorDao).update(doctor);
        testDoctorServiceImpl.update(doctor);
    }

    @Test
    public void getByIdExistingDoctorTest() throws ElementDoesNotExistException {
        long id = 1L;
        Doctor expectedDoctor = new Doctor();
        expectedDoctor.setId(id);
        when(doctorDao.getById(id)).thenReturn(expectedDoctor);
        Doctor actualDoctor = testDoctorServiceImpl.getById(id);
        assertEquals(expectedDoctor, actualDoctor);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void getByIdNotExistingDoctorTest() throws ElementDoesNotExistException {
        long id = -1;
        when(doctorDao.getById(id)).thenThrow(ElementDoesNotExistException.class);
        testDoctorServiceImpl.getById(id);
    }

    @Test
    public void deleteValidDoctorTest() throws SQLIntegrityConstraintViolationException, ElementDoesNotExistException {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        when(recipeService.getRecipeQtyByDoctorId(doctor.getId())).thenReturn(0);
        testDoctorServiceImpl.delete(doctor);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void deleteNotExistingDoctorTest() throws ElementDoesNotExistException, SQLIntegrityConstraintViolationException {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        when(recipeService.getRecipeQtyByDoctorId(doctor.getId())).thenReturn(0);
        doThrow(ElementDoesNotExistException.class).when(doctorDao).delete(doctor);
        testDoctorServiceImpl.delete(doctor);
    }

    @Test(expected = SQLIntegrityConstraintViolationException.class)
    public void deleteDoctorWithRecipesTest() throws SQLIntegrityConstraintViolationException, ElementDoesNotExistException {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        when(recipeService.getRecipeQtyByDoctorId(doctor.getId())).thenReturn(1);
        testDoctorServiceImpl.delete(doctor);
    }

    @Test
    public void getAllTest() {
        List<Doctor> expectedDoctors = new ArrayList<>();
        when(doctorDao.getAll()).thenReturn(expectedDoctors);
        List<Doctor> actualDoctors = testDoctorServiceImpl.getAll();
        assertEquals(expectedDoctors, actualDoctors);
    }
}