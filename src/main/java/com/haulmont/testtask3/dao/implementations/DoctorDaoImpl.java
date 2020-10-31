package com.haulmont.testtask3.dao.implementations;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.dao.interfaces.DoctorDao;
import com.haulmont.testtask3.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link DoctorDao} interface.
 */
@Repository
@Primary
public class DoctorDaoImpl implements DoctorDao {
    private static final String TABLE_NAME = "doctors";
    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "name";
    private static final String SURNAME_FIELD = "surname";
    private static final String PATRONYMIC_FIELD = "patronymic";
    private static final String SPECIALIZATION_FIELD = "specialization";

    private DataSource dataSource;

    public DoctorDaoImpl() {
    }

    @Autowired
    public DoctorDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Doctor entity) {
        String query = "insert into" + " " + TABLE_NAME + " "
                + "("
                + NAME_FIELD + ","
                + SURNAME_FIELD + ","
                + PATRONYMIC_FIELD + ","
                + SPECIALIZATION_FIELD
                + ")"
                + "values (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSurname());
            preparedStatement.setString(3, entity.getPatronymic());
            preparedStatement.setString(4, entity.getSpecialization());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Doctor entity) throws ElementDoesNotExistException {
        String query = "update" + " " + TABLE_NAME + " "
                + "set" + " "
                + NAME_FIELD + " " + "= ?," + " "
                + SURNAME_FIELD + " " + "= ?," + " "
                + PATRONYMIC_FIELD + " " + "= ?," + " "
                + SPECIALIZATION_FIELD + " " + "= ?" + " "
                + "where" + " "
                + ID_FIELD + " " + "= ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSurname());
            preparedStatement.setString(3, entity.getPatronymic());
            preparedStatement.setString(4, entity.getSpecialization());
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
    public Doctor getById(long id) throws ElementDoesNotExistException {
        String query = "select * from" + " " + TABLE_NAME + " "
                + "where" + " " + ID_FIELD + " " + "= ?";
        Doctor doctor = new Doctor();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    doctor.setId(resultSet.getLong(ID_FIELD));
                    doctor.setName(resultSet.getString(NAME_FIELD));
                    doctor.setSurname(resultSet.getString(SURNAME_FIELD));
                    doctor.setSpecialization(resultSet.getString(SPECIALIZATION_FIELD));
                } else {
                    throw new ElementDoesNotExistException();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return doctor;
    }

    @Override
    public void delete(Doctor entity) throws ElementDoesNotExistException {
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
    public List<Doctor> getAll() {
        String query = "select * from" + " " + TABLE_NAME;
        List<Doctor> doctors = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(resultSet.getLong(ID_FIELD));
                doctor.setName(resultSet.getString(NAME_FIELD));
                doctor.setSurname(resultSet.getString(SURNAME_FIELD));
                doctor.setPatronymic(resultSet.getString(PATRONYMIC_FIELD));
                doctor.setSpecialization(resultSet.getString(SPECIALIZATION_FIELD));

                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }
}
