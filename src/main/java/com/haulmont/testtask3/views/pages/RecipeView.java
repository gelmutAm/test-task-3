package com.haulmont.testtask3.views.pages;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.models.Doctor;
import com.haulmont.testtask3.models.Patient;
import com.haulmont.testtask3.models.Priority;
import com.haulmont.testtask3.models.Recipe;
import com.haulmont.testtask3.services.interfaces.DoctorService;
import com.haulmont.testtask3.services.interfaces.PatientService;
import com.haulmont.testtask3.services.interfaces.RecipeService;
import com.haulmont.testtask3.views.components.PageHeader;
import com.haulmont.testtask3.views.components.RecipeDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * Recipes page.
 */
@Route(value = "recipes")
public class RecipeView extends VerticalLayout {
    private static final String APPLY_BUTTON_TEXT = "Apply";
    private static final String ADD_BUTTON_TEXT = "Add";
    private static final String UPDATE_BUTTON_TEXT = "Update";
    private static final String DELETE_BUTTON_TEXT = "Delete";

    private static final String PATIENT_FILTER_PLACEHOLDER = "Filter by patient...";
    private static final String PRIORITY_FILTER_PLACEHOLDER = "Filter by priority...";
    private static final String DESCRIPTION_FILTER_PLACEHOLDER = "Filter by description...";
    private static final String NONE_FILTER_VALUE = "NONE";

    private static final String ID_FIELD = "id";
    private static final String DOCTOR_FIELD = "Doctor";
    private static final String PATIENT_FIELD = "Patient";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String CREATION_DATE_FIELD = "creationDate";
    private static final String VALIDITY_FIELD = "validity";
    private static final String PRIORITY_FIELD = "priority";

    private final RecipeService recipeService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    private Select<Patient> patientFilter = new Select<>();
    private Select<String> priorityFilter = new Select<>();
    private TextField descriptionFilter = new TextField();
    private Button applyButton = new Button(APPLY_BUTTON_TEXT);

    private Button addButton;
    private Button updateButton;
    private Button deleteButton;

    private Grid<Recipe> recipesTable = new Grid<>(Recipe.class);

    @Autowired
    public RecipeView(RecipeService recipeService, PatientService patientService, DoctorService doctorService) {
        this.recipeService = recipeService;
        this.patientService = patientService;
        this.doctorService = doctorService;

        PageHeader pageHeader = new PageHeader();
        add(pageHeader);
        add(getFiltersPanel());
        add(getButtonsBar());
        setTable();
        add(recipesTable);

        setSizeFull();
    }

    /**
     * Returns filters panel.
     *
     * @return filters panel.
     */
    private HorizontalLayout getFiltersPanel() {
        HorizontalLayout filtersPanel = new HorizontalLayout();
        patientFilter.setPlaceholder(PATIENT_FILTER_PLACEHOLDER);
        priorityFilter.setPlaceholder(PRIORITY_FILTER_PLACEHOLDER);
        descriptionFilter.setPlaceholder(DESCRIPTION_FILTER_PLACEHOLDER);
        setSelectsFilterValues();
        addApplyButtonClickListener();
        filtersPanel.add(patientFilter, priorityFilter, descriptionFilter, applyButton);
        return filtersPanel;
    }

    /**
     * Sets selects filters values.
     */
    private void setSelectsFilterValues() {
        List<Patient> patients = patientService.getAll();
        Patient nonePatient = new Patient();
        nonePatient.setName(NONE_FILTER_VALUE);
        nonePatient.setSurname("");
        patients.add(0, nonePatient);
        patientFilter.setItemLabelGenerator(patient -> patient.getName() + " " + patient.getSurname());
        patientFilter.setItems(patients);

        priorityFilter.setItems(NONE_FILTER_VALUE, Priority.NORMAL.name(), Priority.CITO.name(), Priority.STATIM.name());
    }

    /**
     * Sets apply button click listener.
     */
    private void addApplyButtonClickListener() {
        applyButton.addClickListener(e -> {
            Patient patient = patientFilter.getValue();
            Long patientId = null;
            if (patient != null) {
                patientId = patient.getId();
            }
            String priorityValue = priorityFilter.getValue();
            String priority = "";
            if (priorityValue == null) {
                priorityValue = priority;
            }
            if (!priorityValue.equals(NONE_FILTER_VALUE)) {
                priority = priorityValue;
            }
            String description = descriptionFilter.getValue();

            recipesTable.setItems(recipeService.findAll(patientId, priority, description));
        });
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
            RecipeDialog recipeDialog = new RecipeDialog(this, new Recipe());
            recipeDialog.open();
        });
    }

    /**
     * Sets update button click listener.
     */
    private void setUpdateButtonClickListener() {
        updateButton.addClickListener(event -> {
            recipesTable.getSelectionModel().getFirstSelectedItem().ifPresent(recipe -> {
                RecipeDialog recipeDialog = new RecipeDialog(this, recipe);
                recipeDialog.open();
            });
        });
    }

    private Doctor getDoctor(long id) {
        Doctor doctor = null;
        try {
            doctor = doctorService.getById(id);
        } catch (ElementDoesNotExistException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    private Patient getPatient(long id) {
        Patient patient = null;
        try {
            patient = patientService.getById(id);
        } catch (ElementDoesNotExistException e) {
            e.printStackTrace();
        }
        return patient;
    }

    /**
     * Sets delete button click listener.
     */
    private void setDeleteButtonClickListener() {
        deleteButton.addClickListener(event -> {
            recipesTable.getSelectionModel().getFirstSelectedItem().ifPresent(recipe -> {
                try {
                    recipeService.delete(recipe);
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
        recipesTable.setColumns(ID_FIELD, DESCRIPTION_FIELD,
                CREATION_DATE_FIELD, VALIDITY_FIELD, PRIORITY_FIELD);
        recipesTable.addColumn(recipe -> {
            Patient patient = null;
            try {
                patient = patientService.getById(recipe.getPatientId());
            } catch (ElementDoesNotExistException e) {
                e.printStackTrace();
            }
            return patient != null ? patient.getName() + " " + patient.getSurname() : " ";
        }).setHeader(PATIENT_FIELD);

        recipesTable.addColumn(recipe -> {
            Doctor doctor = null;
            try {
                doctor = doctorService.getById(recipe.getDoctorId());
            } catch (ElementDoesNotExistException e) {
                e.printStackTrace();
            }
            return doctor != null ? doctor.getName() + " " + doctor.getSurname() : " ";
        }).setHeader(DOCTOR_FIELD);

        setTableSelectionListener();
        updateTable();
    }

    /**
     * Updates page {@link Grid} rows.
     */
    public void updateTable() {
        List<Recipe> recipes = recipeService.getAll();
        recipesTable.setItems(recipes);
    }

    /**
     * Sets page {@link Grid} selection listener.
     */
    private void setTableSelectionListener() {
        recipesTable.addSelectionListener(e -> {
            if (recipesTable.getSelectionModel().getFirstSelectedItem().isPresent()) {
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

    public RecipeService getRecipeService() {
        return recipeService;
    }

    public DoctorService getDoctorService() {
        return doctorService;
    }

    public PatientService getPatientService() {
        return patientService;
    }
}
