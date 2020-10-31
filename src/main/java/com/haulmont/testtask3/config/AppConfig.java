package com.haulmont.testtask3.config;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.vaadin.artur.helpers.LaunchUtil;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@SpringBootApplication
@ComponentScan("com.haulmont.testtask3")
@EnableVaadin("com.haulmont.testtask3.views")
public class AppConfig extends SpringBootServletInitializer {
    private static final String SQL_SCRIPT_NAME = "/sql_scripts/schema.sql";
    private static final String DRIVER_CLASS_NAME = "org.hsqldb.jdbcDriver";
    private static final String URL = "jdbc:hsqldb:file:db/data";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    @Bean
    public DataSource getDataSource() {
        DataSource dataSource = configureDataSource();
        DatabasePopulatorUtils.execute(createDatabasePopulator(), dataSource);
        return dataSource;
    }

    private DatabasePopulator createDatabasePopulator() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(true);
        databasePopulator.addScript(new ClassPathResource(SQL_SCRIPT_NAME));
        return databasePopulator;
    }

    private DataSource configureDataSource() {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(DRIVER_CLASS_NAME)
                .url(URL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();
        return dataSource;
    }

    @Bean
    public Validator getValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        return validatorFactory.getValidator();
    }

    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(AppConfig.class, args));
    }
}
