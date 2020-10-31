package com.haulmont.testtask3.views.pages;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.models.Patient;
import com.haulmont.testtask3.services.interfaces.PatientService;
import com.haulmont.testtask3.views.components.PageHeader;
import com.haulmont.testtask3.views.components.PatientDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * Patients page.
 */
@Route(value = "patients")
public class PatientView extends VerticalLayout {
    private static final String ADD_BUTTON_TEXT = "Add";
    private static final String UPDATE_BUTTON_TEXT = "Update";
    private static final String DELETE_BUTTON_TEXT = "Delete";

    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "name";
    private static final String SURNAME_FIELD = "surname";
    private static final String PATRONYMIC_FIELD = "patronymic";
    private static final String PHONE_NUMBER_FIELD = "phoneNumber";

    private final PatientService patientService;

    private Button addButton;
    private Button updateButton;
    private Button deleteButton;

    private Grid<Patient> patientTable = new Grid<>(Patient.class);

    @Autowired
    public PatientView(PatientService patientService) {
        this.patientService = patientService;

        PageHeader pageHeader = new PageHeader();
        add(pageHeader);
        add(getButtonsBar());
        setTable();
        add(patientTable);

        setSizeFull();
    }

    /**
     * Returns buttons bar.
     *
     * @return buttons bar.
     */
    private HorizontalLayout getButtonsBar() {
        addButton = new Button(ADD_BUTTON_TEXT);
        updateButton = new Button(UPDATE_BUTTON_TEXT);
        deleteButton = new Button(DELETE_BUTTON_TEXT);

        disableButtons(updateButton, deleteButton);

        setAddButtonClickListener();
        setUpdateButtonClickListener();
        setDeleteButtonClickListener();

        HorizontalLayout buttonsBar = new HorizontalLayout();
        buttonsBar.add(addButton, updateButton, deleteButton);

        return buttonsBar;
    }

    /**
     * Sets add button click listener.
     */
    private void setAddButtonClickListener() {
        addButton.addClickListener(event -> {
            PatientDialog patientDialog = new PatientDialog(this, new Patient());
            patientDialog.open();
        });
    }

    /**
     * Sets update button click listener.
     */
    private void setUpdateButtonClickListener() {
        updateButton.addClickListener(event -> {
            patientTable.getSelectionModel().getFirstSelectedItem().ifPresent(patient -> {
                PatientDialog patientDialog = new PatientDialog(this, patient);
                patientDialog.open();
            });
        });
    }

    /**
     * Sets delete button click listener.
     */
    private void setDeleteButtonClickListener() {
        deleteButton.addClickListener(event -> {
            patientTable.getSelectionModel().getFirstSelectedItem().ifPresent(patient -> {
                try {
                    patientService.delete(patient);
                    updateTable();
                } catch (ElementDoesNotExistException | SQLIntegrityConstraintViolationException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    /**
     * Sets page {@link Grid} to needed state.
     */
    private void setTable() {
        patientTable.setColumns(ID_FIELD, NAME_FIELD, SURNAME_FIELD, PATRONYMIC_FIELD, PHONE_NUMBER_FIELD);
        setTableSelectionListener();
        updateTable();
    }

    /**
     * Updates page {@link Grid} rows.
     */
    public void updateTable() {
        List<Patient> patients = patientService.getAll();
        patientTable.setItems(patients);
    }

    /**
     * Sets page {@link Grid} selection listener.
     */
    private void setTableSelectionListener() {
        patientTable.addSelectionListener(e -> {
            if (patientTable.getSelectionModel().getFirstSelectedItem().isPresent()) {
                enableButtons(updateButton, deleteButton);
            } else {
                disableButtons(updateButton, deleteButton);
            }
        });
    }

    /**
     * Disables specified buttons.
     *
     * @param buttons buttons to be disabled.
     */
    private void disableButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setEnabled(false);
        }
    }

    /**
     * Enables specified buttons.
     *
     * @param buttons buttons to be enabled.
     */
    private void enableButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setEnabled(true);
        }
    }

    public PatientService getPatientService() {
        return patientService;
    }
}
