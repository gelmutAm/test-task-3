package com.haulmont.testtask3.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class Doctor {
    private static final String STRING_FIELDS_REGEX = "^[a-zA-Zа-яА-Я ]{0,50}$";

    private Long id;

    @NotEmpty
    @Pattern(regexp = STRING_FIELDS_REGEX)
    private String name;

    @NotEmpty
    @Pattern(regexp = STRING_FIELDS_REGEX)
    private String surname;

    @Pattern(regexp = STRING_FIELDS_REGEX)
    private String patronymic;

    @NotEmpty
    @Pattern(regexp = STRING_FIELDS_REGEX)
    private String specialization;

    public Doctor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
