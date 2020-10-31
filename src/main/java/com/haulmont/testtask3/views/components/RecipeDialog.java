package com.haulmont.testtask3.views.components;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.models.Doctor;
import com.haulmont.testtask3.models.Patient;
import com.haulmont.testtask3.models.Priority;
import com.haulmont.testtask3.models.Recipe;
import com.haulmont.testtask3.views.pages.RecipeView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.sql.Date;
import java.util.List;

/**
 * Dialog for editing and adding {@link com.haulmont.testtask3.models.Recipe} entities.
 */
public class RecipeDialog extends Dialog {
    private static final String FORM_TITLE = "Recipe";
    private static final String DOCTOR_FIELD = "Doctor";
    private static final String PATIENT_FIELD = "Patient";
    private static final String DESCRIPTION_FIELD = "Description";
    private static final String CREATION_DATE_FIELD = "Creation Date";
    private static final String VALIDITY_FIELD = "Validity";
    private static final String PRIORITY_FIELD = "Priority";

    private static final String EMPTY_FIELD_MESSAGE = "This field can't be empty";
    private static final String WRONG_VALIDITY_FORMAT_MESSAGE = "This field must contain positive digits only. Max size is 3.";
    private static final String WRONG_DESCRIPTION_SIZE_MESSAGE = "Max size of the text is 200 symbols.";

    private static final String INPUT_WIDTH = "300px";

    private static final String OK_BUTTON_TEXT = "Ok";
    private static final String CANCEL_BUTTON_TEXT = "Cancel";

    private Select<Doctor> doctorSelect = new Select<>();
    private Select<Patient> patientSelect = new Select<>();
    private TextArea descriptionFiled = new TextArea(DESCRIPTION_FIELD);
    private TextField creationDateField = new TextField(CREATION_DATE_FIELD);
    private TextField validityField = new TextField(VALIDITY_FIELD);
    private Select<Priority> prioritySelect = new Select<>();

    private Button okButton = new Button(OK_BUTTON_TEXT);
    private final Button cancelButton = new Button(CANCEL_BUTTON_TEXT);

    private RecipeView recipeView;

    private Binder<Recipe> binder = new Binder<>(Recipe.class);

    private Recipe recipe;

    private boolean isRecipeNew;

    public RecipeDialog(RecipeView recipeView, Recipe recipe) {
        this.recipeView = recipeView;
        this.recipe = recipe;
        isRecipeNew = recipe.getId() == null;

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        initSelects();
        initFieldsValidators();
        add(getForm());
    }

    /**
     * Inits selects values.
     */
    private void initSelects() {
        List<Doctor> doctors = recipeView.getDoctorService().getAll();
        doctorSelect.setItemLabelGenerator(doctor -> doctor.getName() + " " + doctor.getSurname());
        doctorSelect.setItems(doctors);

        List<Patient> patients = recipeView.getPatientService().getAll();
        patientSelect.setItemLabelGenerator(patient -> patient.getName() + " " + patient.getSurname());
        patientSelect.setItems(patients);

        prioritySelect.setItemLabelGenerator(Priority::name);
        prioritySelect.setItems(Priority.NORMAL, Priority.CITO, Priority.STATIM);
    }

    /**
     * Inits fields validators.
     */
    private void initFieldsValidators() {
        String validityRegex = "^[1-9][0-9]{0,3}$";

        binder.forField(doctorSelect)
                .asRequired(EMPTY_FIELD_MESSAGE)
                .bind(recipe -> {
                    Doctor doctor = null;
                    if (recipe.getDoctorId() != null) {
                        doctor = getDoctor(recipe.getDoctorId());
                    }
                    return doctor;
                }, (recipe, doctor) -> {
                    recipe.setDoctorId(doctor.getId());
                });

        binder.forField(patientSelect)
                .asRequired(EMPTY_FIELD_MESSAGE)
                .bind(recipe -> {
                    Patient patient = null;
                    if (recipe.getPatientId() != null) {
                        patient = getPatient(recipe.getPatientId());
                    }
                    return patient;
                }, (recipe, patient) -> {
                    recipe.setPatientId(patient.getId());
                });

        binder.forField(creationDateField)
                .bind(recipe -> {
                    return String.valueOf(new Date(System.currentTimeMillis()));
                }, (recipe, dateString) -> {
                    recipe.setCreationDate(Date.valueOf(dateString));
                });

        binder.forField(validityField)
                .asRequired(EMPTY_FIELD_MESSAGE)
                .withValidator(validity -> validity.matches(validityRegex), WRONG_VALIDITY_FORMAT_MESSAGE)
                .withConverter(Integer::valueOf, String::valueOf)
                .bind(Recipe::getValidity, Recipe::setValidity);

        binder.forField(prioritySelect)
                .asRequired(EMPTY_FIELD_MESSAGE)
                .bind(Recipe::getPriority, Recipe::setPriority);

        binder.forField(descriptionFiled)
                .asRequired(EMPTY_FIELD_MESSAGE)
                .withValidator(description -> description.length() <= 200, WRONG_DESCRIPTION_SIZE_MESSAGE)
                .bind(Recipe::getDescription, Recipe::setDescription);

        binder.readBean(recipe);
    }

    private Doctor getDoctor(long id) {
        Doctor doctor = null;
        try {
            doctor = recipeView.getDoctorService().getById(id);
        } catch (ElementDoesNotExistException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    private Patient getPatient(long id) {
        Patient patient = null;
        try {
            patient = recipeView.getPatientService().getById(id);
        } catch (ElementDoesNotExistException e) {
            e.printStackTrace();
        }
        return patient;
    }

    /**
     * Returns dialog form.
     *
     * @return dialog form.
     */
    private FormLayout getForm() {
        FormLayout form = new FormLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        H6 formTitle = new H6(FORM_TITLE);
        setFormFields();
        verticalLayout.add(formTitle, doctorSelect, patientSelect, creationDateField, validityField,
                prioritySelect, descriptionFiled, getButtonsBar());
        form.add(verticalLayout);

        return form;
    }

    /**
     * Sets form fields specified properties.
     */
    private void setFormFields() {
        doctorSelect.setLabel(DOCTOR_FIELD);
        doctorSelect.setWidth(INPUT_WIDTH);
        if (recipe.getDoctorId() != null) {
            doctorSelect.setPlaceholder(getDoctor(recipe.getDoctorId()).getName() + " "
                    + getDoctor(recipe.getDoctorId()).getSurname());
        }
        patientSelect.setLabel(PATIENT_FIELD);
        patientSelect.setWidth(INPUT_WIDTH);
        if (recipe.getPatientId() != null) {
            patientSelect.setPlaceholder(getPatient(recipe.getPatientId()).getName() + " "
                    + getPatient(recipe.getPatientId()).getSurname());
        }
        prioritySelect.setLabel(PRIORITY_FIELD);
        prioritySelect.setWidth(INPUT_WIDTH);

        creationDateField.setEnabled(false);
        setInputsWidth(creationDateField, validityField);

        descriptionFiled.setWidth(INPUT_WIDTH);
    }

    /**
     * Sets width of the specified text fields.
     *
     * @param textFields text fields to be edited.
     */
    private void setInputsWidth(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.setWidth(INPUT_WIDTH);
        }
    }

    /**
     * Returns buttons bar.
     *
     * @return buttons bar.
     */
    private HorizontalLayout getButtonsBar() {
        HorizontalLayout buttonsBar = new HorizontalLayout();
        buttonsBar.add(okButton, cancelButton);
        addOkButtonClickListener();
        addCancelButtonClickListener();
        return buttonsBar;
    }

    /**
     * Sets ok button click listener.
     */
    private void addOkButtonClickListener() {
        okButton.addClickListener(e -> {
            try {
                binder.writeBean(recipe);
                if (isRecipeNew) {
                    recipeView.getRecipeService().add(recipe);
                } else {
                    recipeView.getRecipeService().update(recipe);
                }
                recipeView.updateTable();
                this.close();
            } catch (ValidationException | ElementDoesNotExistException exception) {
                exception.printStackTrace();
            } catch (com.haulmont.testtask3.services.exceptions.ValidationException validationException) {
                validationException.printStackTrace();
            }
        });
    }

    /**
     * Sets cancel button click listener.
     */
    private void addCancelButtonClickListener() {
        cancelButton.addClickListener(e -> {
            this.close();
        });
    }
}
