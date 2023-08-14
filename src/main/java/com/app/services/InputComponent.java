package com.app.services;

import com.app.services.interfaces.CodeComponent;
import com.app.utils.UtilityClass;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;



@Component
@Builder
@NoArgsConstructor(force = true)
@Slf4j
public class InputComponent implements CodeComponent {

    private Map<String, String> customTypes;


    public InputComponent(Map<String, String> customTypes) {
       this.customTypes = customTypes;

    }
    @Override
    public String generateCode() {
        log.info("Generating input component...");
        customTypes.forEach((type, json) -> {
            try {
                log.info("Creating types :: {} from {}", type, json);
                UtilityClass.convertJsonToJavaClass(json, UtilityClass.OUTPUT_CLASS_DIRECTORY, UtilityClass.PACKAGE_NAME, type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return null;
    }
}
