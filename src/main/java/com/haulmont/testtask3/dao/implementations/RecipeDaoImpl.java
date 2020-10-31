package com.haulmont.testtask3.dao.implementations;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.dao.interfaces.RecipeDao;
import com.haulmont.testtask3.models.Priority;
import com.haulmont.testtask3.models.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link RecipeDao} interface.
 */
@Repository
@Primary
public class RecipeDaoImpl implements RecipeDao {
    private static final String TABLE_NAME = "recipes";
    private static final String ID_FIELD = "id";
    private static final String PATIENT_ID_FIELD = "patient_id";
    private static final String DOCTOR_ID_FIELD = "doctor_id";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String CREATION_DATE_FIELD = "creation_date";
    private static final String VALIDITY_FIELD = "validity";
    private static final String PRIORITY_FIELD = "priority";

    private DataSource dataSource;

    public RecipeDaoImpl() {
    }

    @Autowired
    public RecipeDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Recipe entity) {
        String query = "insert into" + " " + TABLE_NAME + " "
                + "("
                + PATIENT_ID_FIELD + ","
                + DOCTOR_ID_FIELD + ","
                + DESCRIPTION_FIELD + ","
                + CREATION_DATE_FIELD + ","
                + VALIDITY_FIELD + ","
                + PRIORITY_FIELD
                + ")"
                + "values (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, entity.getPatientId());
            preparedStatement.setLong(2, entity.getDoctorId());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setObject(4, entity.getCreationDate());
            preparedStatement.setInt(5, entity.getValidity());
            preparedStatement.setString(6, entity.getPriority().name());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Recipe entity) throws ElementDoesNotExistException {
        String query = "update" + " " + TABLE_NAME + " "
                + "set" + " "
                + PATIENT_ID_FIELD + " " + "= ?," + " "
                + DOCTOR_ID_FIELD + " " + "= ?," + " "
                + DESCRIPTION_FIELD + " " + "= ?," + " "
                + CREATION_DATE_FIELD + " " + "= ?," + " "
                + VALIDITY_FIELD + " " + "= ?," + " "
                + PRIORITY_FIELD + " " + "= ?" + " "
                + "where" + " "
                + ID_FIELD + " " + "= ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, entity.getPatientId());
            preparedStatement.setLong(2, entity.getDoctorId());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setObject(4, entity.getCreationDate());
            preparedStatement.setInt(5, entity.getValidity());
            preparedStatement.setString(6, entity.getPriority().name());
            preparedStatement.setLong(7, entity.getId());
            int affectedRowsQty = preparedStatement.executeUpdate();

            if (affectedRowsQty == 0) {
                throw new ElementDoesNotExistException();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Recipe getById(long id) throws ElementDoesNotExistException {
        String query = "select * from" + " " + TABLE_NAME + " "
                + "where" + " " + ID_FIELD + " " + "= ?";
        Recipe recipe = new Recipe();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    recipe.setId(resultSet.getLong(ID_FIELD));
                    recipe.setPatientId(resultSet.getLong(PATIENT_ID_FIELD));
                    recipe.setDoctorId(resultSet.getLong(DOCTOR_ID_FIELD));
                    recipe.setDescription(resultSet.getString(DESCRIPTION_FIELD));
                    recipe.setCreationDate(resultSet.getDate(CREATION_DATE_FIELD));
                    recipe.setValidity(resultSet.getInt(VALIDITY_FIELD));
                    recipe.setPriority(Priority.valueOf(resultSet.getString(PRIORITY_FIELD)));
                } else {
                    throw new ElementDoesNotExistException();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return recipe;
    }

    @Override
    public void delete(Recipe entity) throws ElementDoesNotExistException {
        String query = "delete from" + " " + TABLE_NAME + " " + "where" + " " + ID_FIELD + " " + "= ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, entity.getId());
            int affectedRowsQty = preparedStatement.executeUpdate();

            if (affectedRowsQty == 0) {
                throw new ElementDoesNotExistException();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Recipe> getAll() {
        String query = "select * from" + " " + TABLE_NAME;
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Recipe recipe = new Recipe();
                recipe.setId(resultSet.getLong(ID_FIELD));
                recipe.setPatientId(resultSet.getLong(PATIENT_ID_FIELD));
                recipe.setDoctorId(resultSet.getLong(DOCTOR_ID_FIELD));
                recipe.setDescription(resultSet.getString(DESCRIPTION_FIELD));
                recipe.setCreationDate(resultSet.getDate(CREATION_DATE_FIELD));
                recipe.setValidity(resultSet.getInt(VALIDITY_FIELD));
                recipe.setPriority(Priority.valueOf(resultSet.getString(PRIORITY_FIELD)));

                recipes.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    @Override
    public int getRecipeQtyByPatientId(long patientId) {
        String query = "select count(*) from" + " " + TABLE_NAME + " "
                + "where" + " " + PATIENT_ID_FIELD + " " + "= ?";
        int recipeQty = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, patientId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    recipeQty = resultSet.getInt(1);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return recipeQty;
    }

    @Override
    public int getRecipeQtyByDoctorId(long doctorId) {
        String query = "select count(*) from" + " " + TABLE_NAME + " "
                + "where" + " " + DOCTOR_ID_FIELD + " " + "= ?";
        int recipeQty = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, doctorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    recipeQty = resultSet.getInt(1);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return recipeQty;
    }

    @Override
    public List<Recipe> findAll(Long patientId, String priority, String description) {
        String query = "select * from" + " " + TABLE_NAME + " "
                + "where" + " " + PRIORITY_FIELD + " " + "like ? and" + " "
                + DESCRIPTION_FIELD + " " + "like ?";
        String patientNotNullQuery = " " + "and" + " " + PATIENT_ID_FIELD + " " + "= ?";
        List<Recipe> recipes = new ArrayList<>();
        if (patientId == null && priority == null && description.equals("")) {
            recipes = getAll();
        }

        if (patientId != null) {
            query = query + patientNotNullQuery;
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + priority);
            preparedStatement.setString(2, "%" + description + "%");
            if (patientId != null) {
                preparedStatement.setLong(3, patientId);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Recipe recipe = new Recipe();
                    recipe.setId(resultSet.getLong(ID_FIELD));
                    recipe.setPatientId(resultSet.getLong(PATIENT_ID_FIELD));
                    recipe.setDoctorId(resultSet.getLong(DOCTOR_ID_FIELD));
                    recipe.setDescription(resultSet.getString(DESCRIPTION_FIELD));
                    recipe.setCreationDate(resultSet.getDate(CREATION_DATE_FIELD));
                    recipe.setValidity(resultSet.getInt(VALIDITY_FIELD));
                    recipe.setPriority(Priority.valueOf(resultSet.getString(PRIORITY_FIELD)));

                    recipes.add(recipe);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return recipes;
    }
}
