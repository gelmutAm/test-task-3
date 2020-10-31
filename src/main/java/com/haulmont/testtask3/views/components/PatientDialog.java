package com.haulmont.testtask3.views.components;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.models.Patient;
import com.haulmont.testtask3.views.pages.PatientView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

/**
 * Dialog for editing and adding {@link com.haulmont.testtask3.models.Patient} entities.
 */
public class PatientDialog extends Dialog {
    private static final String FORM_TITLE = "Patient";
    private static final String NAME_FIELD_LABEL = "Name";
    private static final String SURNAME_FIELD_LABEL = "Surname";
    private static final String PATRONYMIC_FIELD_LABEL = "Patronymic";
    private static final String PHONE_NUMBER_FIELD_LABEL = "Phone Number";

    private static final String EMPTY_FIELD_MESSAGE = "This field can't be empty";
    private static final String WRONG_PATTERN_FIELD_MESSAGE = "This field must contain letters only."
            + " Max size is 50.";
    private static final String WRONG_PHONE_NUMBER_FORMAT_MESSAGE = "This field must contain digits only."
            + " Required size is 11.";

    private static final String INPUT_WIDTH = "300px";

    private static final String OK_BUTTON_TEXT = "Ok";
    private static final String CANCEL_BUTTON_TEXT = "Cancel";

    private final TextField nameField = new TextField(NAME_FIELD_LABEL);
    private final TextField surnameField = new TextField(SURNAME_FIELD_LABEL);
    private final TextField patronymicField = new TextField(PATRONYMIC_FIELD_LABEL);
    private final TextField phoneNumberField = new TextField(PHONE_NUMBER_FIELD_LABEL);

    private Button okButton = new Button(OK_BUTTON_TEXT);
    private final Button cancelButton = new Button(CANCEL_BUTTON_TEXT);

    private PatientView patientView;

    Binder<Patient> binder = new Binder<>(Patient.class);

    private Patient patient;

    private boolean isPatientNew;

    public PatientDialog(PatientView patientView, Patient patient) {
        this.patientView = patientView;
        this.patient = patient;
        isPatientNew = patient.getId() == null;

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        initFieldsValidators();
        add(getForm());
    }

    /**
     * Inits fields validators.
     */
    private void initFieldsValidators() {
        String stringFieldReqex = "^[a-zA-Zа-яА-Я ]{0,50}$";
        String phoneNumberRegex = "^[0-9]{11}$";

        binder.forField(nameField)
                .asRequired(EMPTY_FIELD_MESSAGE)
                .withValidator(name -> name.matches(stringFieldReqex), WRONG_PATTERN_FIELD_MESSAGE)
                .bind(Patient::getName, Patient::setName);

        binder.forField(surnameField)
                .asRequired(EMPTY_FIELD_MESSAGE)
                .withValidator(surname -> surname.matches(stringFieldReqex), WRONG_PATTERN_FIELD_MESSAGE)
                .bind(Patient::getSurname, Patient::setSurname);

        binder.forField(patronymicField)
                .withValidator(patronymic -> patronymic.matches(stringFieldReqex), WRONG_PATTERN_FIELD_MESSAGE)
                .bind(Patient::getPatronymic, Patient::setPatronymic);

        binder.forField(phoneNumberField)
                .asRequired(EMPTY_FIELD_MESSAGE)
                .withValidator(specialization -> specialization.matches(phoneNumberRegex),
                        WRONG_PHONE_NUMBER_FORMAT_MESSAGE)
                .bind(Patient::getPhoneNumber, Patient::setPhoneNumber);

        binder.readBean(patient);
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
        setInputsWidth(nameField, surnameField, patronymicField, phoneNumberField);
        verticalLayout.add(formTitle, nameField, surnameField, patronymicField, phoneNumberField, getButtonsBar());
        form.add(verticalLayout);

        return form;
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
                binder.writeBean(patient);
                if (isPatientNew) {
                    patientView.getPatientService().add(patient);
                } else {
                    patientView.getPatientService().update(patient);
                }
                patientView.updateTable();
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
