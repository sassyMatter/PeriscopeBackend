package com.app.services.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ListIterator;

@Service
@Slf4j
public class CodeWriterService {



    @Value("${com.userSpace.targetParentDirectory}")
    public static String targetParentDir;

    /**
     * This will be used to write code to the user project template
     *
     */
    public void writeToFile(String input, String type, String projectDir) {


        try {

            String filePath = null;

            switch (type) {
                case "restInterface":
                    filePath = projectDir + "/src/main/java/com/app/controllers/UserEndpoint.java";
                    break;
                case "function":
                    filePath = projectDir + "/src/main/java/com/app/services/CustomCodeService.java";
                    break;
                case "input":
                    filePath = projectDir + "src/main/java/com/app/models/customModels";
                    break;
                case "queue":
                    filePath = projectDir + "src/main/resources";
                    break;
                case "database":
                    filePath = projectDir + "src/main/resources";;
                    break;
                default:
                    // Handle the default case if needed
                    log.error("Type mismatch while writing to file");
                    break;
            }
            log.info("File path for node {} ", filePath);


            Path file = Path.of(filePath);
            log.info("file:: {} ", file);
            List<String> lines = Files.readAllLines(file);

            // Find the index of the last closing curly brace
            int lastClosingBraceIndex = findLastClosingBraceIndex(lines);

            // Validate the index
            if (lastClosingBraceIndex == -1) {
                throw new IllegalStateException("Closing brace not found in the file");
            }

            // Insert the input string just before the last closing curly brace
            lines.add(lastClosingBraceIndex, input);

            // Write the modified content back to the file
            Files.write(file, lines, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     *
     * @param lines
     * @return int
     */
    private int findLastClosingBraceIndex(List<String> lines) {
        ListIterator<String> iterator = lines.listIterator(lines.size());

        while (iterator.hasPrevious()) {
            String line = iterator.previous().trim();
            if (line.equals("}")) {
                return iterator.nextIndex();
            }
        }

        return -1;
    }






}
