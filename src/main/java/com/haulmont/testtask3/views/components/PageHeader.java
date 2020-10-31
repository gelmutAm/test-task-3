package com.haulmont.testtask3.views.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * Page header class.
 */
public class PageHeader extends Header {
    private static final String DOCTORS_LINK_NAME = "Doctors";
    private static final String PATIENTS_LINK_NAME = "Patients";
    private static final String RECIPES_LINK_NAME = "Recipes";

    private static final String DOCTORS_LINK = "/";
    private static final String PATIENTS_LINK = "/patients";
    private static final String RECIPES_LINK = "/recipes";

    private static final String HEADER_HEIGHT = "40px";

    public PageHeader() {
        Anchor doctorsLink = new Anchor(DOCTORS_LINK);
        doctorsLink.setText(DOCTORS_LINK_NAME);
        Anchor patientsLink = new Anchor(PATIENTS_LINK);
        patientsLink.setText(PATIENTS_LINK_NAME);
        Anchor recipesLink = new Anchor(RECIPES_LINK);
        recipesLink.setText(RECIPES_LINK_NAME);

        HorizontalLayout headerLinks = new HorizontalLayout();
        headerLinks.add(doctorsLink, patientsLink, recipesLink);

        setWidthFull();
        setHeight(HEADER_HEIGHT);
        getStyle().set("display", "flex");
        getStyle().set("flex-direction", "column");
        getStyle().set("justify-content", "center");
        getStyle().set("border-bottom", "1px solid hsla(214, 53%, 23%, 0.16)");
        getStyle().set("margin-bottom", "20px");
        add(headerLinks);
    }
}
