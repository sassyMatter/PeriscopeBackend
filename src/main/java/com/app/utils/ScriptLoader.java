package com.app.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Component
public class ScriptLoader {

    private final ResourceLoader resourceLoader;

    public ScriptLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public File loadScriptFile(String scriptName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:scripts/" + scriptName);
        try (InputStream inputStream = resource.getInputStream()) {
            File tempFile = File.createTempFile(scriptName, null);
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return tempFile;
        }
    }
}
