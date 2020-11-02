package com.haulmont.testtask3.services.implementations;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.dao.interfaces.PatientDao;
import com.haulmont.testtask3.models.Patient;
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
public class PatientServiceImplTest {

    @Mock
    private PatientDao patientDao;

    @Mock
    private RecipeService recipeService;

    @Mock
    private Validator validator;

    @InjectMocks
    private PatientServiceImpl testPatientServiceImpl;

    @Test
    public void addValidPatientTest() throws ValidationException {
        Patient patient = new Patient();
        doNothing().when(patientDao).add(patient);
        testPatientServiceImpl.add(patient);
    }

    @Test(expected = ValidationException.class)
    public void addInvalidPatientTest() throws ValidationException {
        Patient patient = new Patient();
        Set<ConstraintViolation<Patient>> set = new HashSet<>();
        set.add(null);
        when(validator.validate(patient)).thenReturn(set);
        testPatientServiceImpl.add(patient);
    }

    @Test
    public void updateValidPatientTest() throws ElementDoesNotExistException, ValidationException {
        Patient patient = new Patient();
        patient.setId(1L);
        doNothing().when(patientDao).update(patient);
        testPatientServiceImpl.update(patient);
    }

    @Test(expected = ValidationException.class)
    public void updateInvalidPatientTest() throws ValidationException, ElementDoesNotExistException {
        Patient patient = new Patient();
        testPatientServiceImpl.update(patient);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void updateNotExistingPatientTest() throws ElementDoesNotExistException, ValidationException {
        Patient patient = new Patient();
        patient.setId(-1L);
        doThrow(ElementDoesNotExistException.class).when(patientDao).update(patient);
        testPatientServiceImpl.update(patient);
    }

    @Test
    public void getByIdExistingPatientTest() throws ElementDoesNotExistException {
        long id = 1L;
        Patient expectedPatient = new Patient();
        expectedPatient.setId(id);
        when(patientDao.getById(id)).thenReturn(expectedPatient);
        Patient actualPatient = testPatientServiceImpl.getById(id);
        assertEquals(expectedPatient, actualPatient);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void getByIdNotExistingPatientTest() throws ElementDoesNotExistException {
        long id = -1;
        when(patientDao.getById(id)).thenThrow(ElementDoesNotExistException.class);
        testPatientServiceImpl.getById(id);
    }

    @Test
    public void deleteExistingPatientTest() throws SQLIntegrityConstraintViolationException, ElementDoesNotExistException {
        Patient patient = new Patient();
        patient.setId(1L);
        doNothing().when(patientDao).delete(patient);
        testPatientServiceImpl.delete(patient);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void deleteNotExistingPatientTest() throws SQLIntegrityConstraintViolationException, ElementDoesNotExistException {
        Patient patient = new Patient();
        patient.setId(-1L);
        doThrow(ElementDoesNotExistException.class).when(patientDao).delete(patient);
        testPatientServiceImpl.delete(patient);
    }

    @Test(expected = SQLIntegrityConstraintViolationException.class)
    public void deletePatientWithRecipesTest() throws SQLIntegrityConstraintViolationException, ElementDoesNotExistException {
        Patient patient = new Patient();
        patient.setId(1L);
        when(recipeService.getRecipeQtyByPatientId(patient.getId())).thenReturn(1);
        testPatientServiceImpl.delete(patient);
    }

    @Test
    public void getAllTest() {
        List<Patient> expectedPatients = new ArrayList<>();
        when(patientDao.getAll()).thenReturn(expectedPatients);
        List<Patient> actualPatients = testPatientServiceImpl.getAll();
        assertEquals(expectedPatients, actualPatients);
    }
}