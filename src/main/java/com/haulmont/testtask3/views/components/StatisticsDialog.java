package com.haulmont.testtask3.views.components;

import com.haulmont.testtask3.models.Doctor;
import com.haulmont.testtask3.views.pages.DoctorView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

/**
 * Dialog needed to show {@link com.haulmont.testtask3.models.Doctor} statistics.
 */
public class StatisticsDialog extends Dialog {
    private static final String FORM_TITLE = "Doctor Statistics";
    private static final String NAME_FIELD_LABEL = "Name";
    private static final String SURNAME_FIELD_LABEL = "Surname";
    private static final String PATRONYMIC_FIELD_LABEL = "Patronymic";
    private static final String SPECIALIZATION_FIELD_LABEL = "Specialization";
    private static final String STATISTICS_FIELD_LABEL = "Statistics";

    private static final String INPUT_WIDTH = "300px";

    private static final String OK_BUTTON_TEXT = "Ok";

    private final TextField nameField = new TextField(NAME_FIELD_LABEL);
    private final TextField surnameField = new TextField(SURNAME_FIELD_LABEL);
    private final TextField patronymicField = new TextField(PATRONYMIC_FIELD_LABEL);
    private final TextField specializationField = new TextField(SPECIALIZATION_FIELD_LABEL);
    private final TextField statisticsField = new TextField(STATISTICS_FIELD_LABEL);

    private Button okButton = new Button(OK_BUTTON_TEXT);

    private DoctorView doctorView;

    private Doctor doctor;

    private Binder<Doctor> binder = new Binder<>(Doctor.class);

    public StatisticsDialog(DoctorView doctorView, Doctor doctor) {
        this.doctorView = doctorView;
        this.doctor = doctor;

        setCloseOnEsc(false);
        bind();
        setStatisticsFieldValue();
        setCloseOnOutsideClick(false);
        add(getForm());
    }

    private void bind() {
        binder.forField(nameField).bind(Doctor::getName, Doctor::setName);
        binder.forField(surnameField).bind(Doctor::getSurname, Doctor::setSurname);
        binder.forField(patronymicField).bind(Doctor::getPatronymic, Doctor::setPatronymic);
        binder.forField(specializationField).bind(Doctor::getSpecialization, Doctor::setSpecialization);
        binder.readBean(doctor);
    }

    /**
     * Sets statistics field value.
     */
    private void setStatisticsFieldValue() {
        int quantity = doctorView.getRecipeService().getRecipeQtyByDoctorId(doctor.getId());
        String value = String.valueOf(quantity);
        statisticsField.setValue(value);
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
        setInputsWidth(nameField, surnameField, patronymicField, specializationField, statisticsField);
        verticalLayout.add(formTitle, nameField, surnameField, patronymicField,
                specializationField, statisticsField, getButtonsBar());
        form.add(verticalLayout);

        return form;
    }

    /**
     * Sets width of the specified text fields.
     *
     * @param textFields text fields to be edited
     */
    private void setInputsWidth(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.setWidth(INPUT_WIDTH);
            textField.setEnabled(false);
        }
    }

    /**
     * Returns buttons bar.
     *
     * @return buttons bar.
     */
    private HorizontalLayout getButtonsBar() {
        HorizontalLayout buttonsBar = new HorizontalLayout();
        buttonsBar.add(okButton);
        addOkClickListener();
        return buttonsBar;
    }

    /**
     * Sets cancel button click listener.
     */
    private void addOkClickListener() {
        okButton.addClickListener(e -> {
            this.close();
        });
    }
}
