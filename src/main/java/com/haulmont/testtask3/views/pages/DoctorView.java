package com.haulmont.testtask3.views.pages;

import com.haulmont.testtask3.dao.exceptions.ElementDoesNotExistException;
import com.haulmont.testtask3.models.Doctor;
import com.haulmont.testtask3.services.interfaces.DoctorService;
import com.haulmont.testtask3.services.interfaces.RecipeService;
import com.haulmont.testtask3.views.components.DoctorDialog;
import com.haulmont.testtask3.views.components.PageHeader;
import com.haulmont.testtask3.views.components.StatisticsDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * Doctors page.
 */
@Route(value = "")
public class DoctorView extends VerticalLayout {
    private static final String ADD_BUTTON_TEXT = "Add";
    private static final String UPDATE_BUTTON_TEXT = "Update";
    private static final String DELETE_BUTTON_TEXT = "Delete";
    private static final String STATISTICS_BUTTON_TEXT = "Statistics";

    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "name";
    private static final String SURNAME_FIELD = "surname";
    private static final String PATRONYMIC_FIELD = "patronymic";
    private static final String SPECIALIZATION_FIELD = "specialization";

    private final DoctorService doctorService;
    private final RecipeService recipeService;

    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button statisticsButton;

    private Grid<Doctor> doctorsTable = new Grid<>(Doctor.class);

    @Autowired
    public DoctorView(DoctorService doctorService, RecipeService recipeService) {
        this.doctorService = doctorService;
        this.recipeService = recipeService;

        PageHeader pageHeader = new PageHeader();
        add(pageHeader);
        add(getButtonsBar());
        setTable();
        add(doctorsTable);

        setSizeFull();
    }

    /**
     * Returns buttons bar.
     *
     * @return buttons bar.
     */
    private HorizontalLayout getButtonsBar() {
        HorizontalLayout buttonsBar = new HorizontalLayout();
        addButton = new Button(ADD_BUTTON_TEXT);
        updateButton = new Button(UPDATE_BUTTON_TEXT);
        deleteButton = new Button(DELETE_BUTTON_TEXT);
        statisticsButton = new Button(STATISTICS_BUTTON_TEXT);

        disableButtons(updateButton, deleteButton, statisticsButton);

        setAddButtonClickListener();
        setUpdateButtonClickListener();
        setDeleteButtonClickListener();
        setStatisticsButtonClickListener();

        buttonsBar.add(addButton, updateButton, deleteButton, statisticsButton);

        return buttonsBar;
    }

    /**
     * Sets add button click listener.
     */
    private void setAddButtonClickListener() {
        addButton.addClickListener(e -> {
            DoctorDialog doctorDialog = new DoctorDialog(this, new Doctor());
            doctorDialog.open();
        });
    }

    /**
     * Sets update button click listener.
     */
    private void setUpdateButtonClickListener() {
        updateButton.addClickListener(e -> {
            doctorsTable.getSelectionModel().getFirstSelectedItem().ifPresent(doctor -> {
                DoctorDialog doctorDialog = new DoctorDialog(this, doctor);
                doctorDialog.open();
            });
        });
    }

    /**
     * Sets delete button click listener.
     */
    private void setDeleteButtonClickListener() {
        deleteButton.addClickListener(e -> {
            doctorsTable.getSelectionModel().getFirstSelectedItem().ifPresent(doctor -> {
                try {
                    doctorService.delete(doctor);
                    updateTable();
                } catch (ElementDoesNotExistException | SQLIntegrityConstraintViolationException exception) {
                    exception.printStackTrace();
                }
            });
        });
    }

    /**
     * Sets statistics button click listener.
     */
    private void setStatisticsButtonClickListener() {
        statisticsButton.addClickListener(e -> {
            doctorsTable.getSelectionModel().getFirstSelectedItem().ifPresent(doctor -> {
                StatisticsDialog statisticsDialog = new StatisticsDialog(this, doctor);
                statisticsDialog.open();
            });
        });
    }

    /**
     * Sets page {@link Grid} to needed state.
     */
    private void setTable() {
        doctorsTable.setColumns(ID_FIELD, NAME_FIELD, SURNAME_FIELD, PATRONYMIC_FIELD, SPECIALIZATION_FIELD);
        setTableSelectionListener();
        updateTable();
    }

    /**
     * Updates page {@link Grid} rows.
     */
    public void updateTable() {
        List<Doctor> doctorList = doctorService.getAll();
        doctorsTable.setItems(doctorList);
    }

    /**
     * Sets page {@link Grid} selection listener.
     */
    private void setTableSelectionListener() {
        doctorsTable.addSelectionListener(e -> {
            if (doctorsTable.getSelectionModel().getFirstSelectedItem().isPresent()) {
                enableButtons(updateButton, deleteButton, statisticsButton);
            } else {
                disableButtons(updateButton, deleteButton, statisticsButton);
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

    public DoctorService getDoctorService() {
        return doctorService;
    }

    public RecipeService getRecipeService() {
        return recipeService;
    }
}
