package com.haulmont.testtask3.dao.implementations;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.dao.interfaces.PatientDao;
import com.haulmont.testtask3.models.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link PatientDao} interface.
 */
@Repository
@Primary
public class PatientDaoImpl implements PatientDao {
    private static final String TABLE_NAME = "patients";
    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "name";
    private static final String SURNAME_FIELD = "surname";
    private static final String PATRONYMIC_FIELD = "patronymic";
    private static final String PHONE_NUMBER_FIELD = "phone_number";

    private DataSource dataSource;

    public PatientDaoImpl() {
    }

    @Autowired
    public PatientDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Patient entity) {
        String query = "insert into" + " " + TABLE_NAME + " "
                + "("
                + NAME_FIELD + ","
                + SURNAME_FIELD + ","
                + PATRONYMIC_FIELD + ","
                + PHONE_NUMBER_FIELD
                + ")"
                + "values (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSurname());
            preparedStatement.setString(3, entity.getPatronymic());
            preparedStatement.setString(4, entity.getPhoneNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Patient entity) throws ElementDoesNotExistException {
        String query = "update" + " " + TABLE_NAME + " "
                + "set" + " "
                + NAME_FIELD + " " + "= ?," + " "
                + SURNAME_FIELD + " " + "= ?," + " "
                + PATRONYMIC_FIELD + " " + "= ?," + " "
                + PHONE_NUMBER_FIELD + " " + "= ?" + " "
                + "where" + " "
                + ID_FIELD + " " + "= ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSurname());
            preparedStatement.setString(3, entity.getPatronymic());
            preparedStatement.setString(4, entity.getPhoneNumber());
            preparedStatement.setLong(5, entity.getId());
            int affectedRowsQty = preparedStatement.executeUpdate();

            if (affectedRowsQty == 0) {
                throw new ElementDoesNotExistException();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Patient getById(long id) throws ElementDoesNotExistException {
        String query = "select * from" + " " + TABLE_NAME + " "
                + "where" + " " + ID_FIELD + " " + "= ?";
        Patient patient = new Patient();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    patient.setId(resultSet.getLong(ID_FIELD));
                    patient.setName(resultSet.getString(NAME_FIELD));
                    patient.setSurname(resultSet.getString(SURNAME_FIELD));
                    patient.setPhoneNumber(resultSet.getString(PHONE_NUMBER_FIELD));
                } else {
                    throw new ElementDoesNotExistException();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return patient;
    }

    @Override
    public void delete(Patient entity) throws ElementDoesNotExistException {
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
    public List<Patient> getAll() {
        String query = "select * from" + " " + TABLE_NAME;
        List<Patient> patients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Patient patient = new Patient();
                patient.setId(resultSet.getLong(ID_FIELD));
                patient.setName(resultSet.getString(NAME_FIELD));
                patient.setSurname(resultSet.getString(SURNAME_FIELD));
                patient.setPatronymic(resultSet.getString(PATRONYMIC_FIELD));
                patient.setPhoneNumber(resultSet.getString(PHONE_NUMBER_FIELD));

                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }
}
