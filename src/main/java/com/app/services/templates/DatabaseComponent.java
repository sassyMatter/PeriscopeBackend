package com.app.services.templates;

import com.app.services.interfaces.CodeComponent;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


@Builder
@Component
@NoArgsConstructor(force = true)
@Slf4j
public class DatabaseComponent implements CodeComponent {
    private final List<String> tableDefinitions;


    // will have to initialize jdbc template with datasource and all to initiate Database component

    private JdbcTemplate jdbcTemplate;

    public DatabaseComponent(List<String> tableDefinitions, JdbcTemplate jdbcTemplate) {
        this.tableDefinitions = tableDefinitions;
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public String generateCode() {
        // Execute DDL statements using JdbcTemplate


        return tableDefinitions.toString();
    }
}
