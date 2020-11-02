package com.haulmont.testtask3.services.implementations;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.dao.interfaces.RecipeDao;
import com.haulmont.testtask3.models.Recipe;
import com.haulmont.testtask3.services.exceptions.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {

    @Mock
    private RecipeDao recipeDao;

    @Mock
    private Validator validator;

    @InjectMocks
    private RecipeServiceImpl testRecipeServiceImpl;

    @Test
    public void addValidRecipeTest() throws ValidationException {
        Recipe recipe = new Recipe();
        doNothing().when(recipeDao).add(recipe);
        testRecipeServiceImpl.add(recipe);
    }

    @Test(expected = ValidationException.class)
    public void addInvalidRecipeTest() throws ValidationException {
        Recipe recipe = new Recipe();
        Set<ConstraintViolation<Recipe>> set = new HashSet<>();
        set.add(null);
        when(validator.validate(recipe)).thenReturn(set);
        testRecipeServiceImpl.add(recipe);
    }

    @Test
    public void updateValidRecipeTest() throws ElementDoesNotExistException, ValidationException {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        doNothing().when(recipeDao).update(recipe);
        testRecipeServiceImpl.update(recipe);
    }

    @Test(expected = ValidationException.class)
    public void updateInvalidRecipeTest() throws ValidationException, ElementDoesNotExistException {
        Recipe recipe = new Recipe();
        testRecipeServiceImpl.update(recipe);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void updateNotExistingRecipeTest() throws ValidationException, ElementDoesNotExistException {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        doThrow(ElementDoesNotExistException.class).when(recipeDao).update(recipe);
        testRecipeServiceImpl.update(recipe);
    }

    @Test
    public void getByIdExistingRecipeTest() throws ElementDoesNotExistException {
        long id = 1L;
        Recipe expectedRecipe = new Recipe();
        expectedRecipe.setId(id);
        when(recipeDao.getById(id)).thenReturn(expectedRecipe);
        Recipe actualRecipe = testRecipeServiceImpl.getById(id);
        assertEquals(expectedRecipe, actualRecipe);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void getByIdNotExistingRecipeTest() throws ElementDoesNotExistException {
        long id = -1;
        when(recipeDao.getById(id)).thenThrow(ElementDoesNotExistException.class);
        testRecipeServiceImpl.getById(id);
    }

    @Test
    public void deleteValidRecipeTest() throws ElementDoesNotExistException {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        doNothing().when(recipeDao).delete(recipe);
        testRecipeServiceImpl.delete(recipe);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void deleteNotExistingRecipeTest() throws ElementDoesNotExistException {
        Recipe recipe = new Recipe();
        recipe.setId(-1L);
        doThrow(ElementDoesNotExistException.class).when(recipeDao).delete(recipe);
        testRecipeServiceImpl.delete(recipe);
    }

    @Test
    public void getAllTest() {
        List<Recipe> expectedRecipes = new ArrayList<>();
        when(recipeDao.getAll()).thenReturn(expectedRecipes);
        List<Recipe> actualRecipes = testRecipeServiceImpl.getAll();
        assertEquals(expectedRecipes, actualRecipes);
    }

    @Test
    public void getRecipeQtyByPatientIdTest() {
        long id = 1L;
        int expectedQty = 0;
        when(recipeDao.getRecipeQtyByPatientId(id)).thenReturn(expectedQty);
        int actualQty = testRecipeServiceImpl.getRecipeQtyByPatientId(id);
        assertEquals(expectedQty, actualQty);
    }

    @Test
    public void getRecipeQtyByDoctorIdTest() {
        long id = 1L;
        int expectedQty = 0;
        when(recipeDao.getRecipeQtyByDoctorId(id)).thenReturn(expectedQty);
        int actualQty = testRecipeServiceImpl.getRecipeQtyByDoctorId(id);
        assertEquals(expectedQty, actualQty);
    }

    @Test
    public void findAllTest() {
        long patientId = 1L;
        String priority = "";
        String description = "";
        List<Recipe> expectedRecipes = new ArrayList<>();
        when(recipeDao.findAll(patientId, priority, description)).thenReturn(expectedRecipes);
        List<Recipe> actualRecipes = testRecipeServiceImpl.findAll(patientId, priority, description);
        assertEquals(expectedRecipes, actualRecipes);
    }
}