package com.app.services.templates;

import com.app.services.interfaces.CodeComponent;
import com.app.utils.UtilityClass;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;



@Component
@Builder
@Setter
@Getter
@NoArgsConstructor(force = true)
@Slf4j
public class InputComponent implements CodeComponent {

    private Map<String, String> customTypes;

    private String outputClassDirectory;


    public InputComponent(Map<String, String> customTypes, String outputClassDirectory) {
       this.customTypes = customTypes;
       this.outputClassDirectory = outputClassDirectory;

    }
    @Override
    public String generateCode() {
        log.info("Generating input component...");
        customTypes.forEach((type, json) -> {
            try {
                log.info("Creating types :: {} from {}", type, json);
                UtilityClass.convertJsonToJavaClass(json, new File(outputClassDirectory + "/src/main/java/"), UtilityClass.PACKAGE_NAME, type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return null;
    }
}
