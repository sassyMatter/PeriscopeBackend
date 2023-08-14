package com.app.services;

import com.app.controllers.Converter;
import lombok.extern.slf4j.Slf4j;
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

    /**
     *
     * @param input
     * @param type
     */
    public void writeToFile(String input, String type) {
        try {

            String filePath = null;
            if("rest".equals(type)){
//                 filePath = Converter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//                   filePath = Converter.class.getResource("/Converter.class").getPath();
                filePath = "/Users/nirbhay11.singh/IdeaProjects/PeriscopeBackend/src/main/java/com/app/controllers/Converter.java";
            }else {
//                 filePath = CodeWriterService.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                filePath = "/Users/nirbhay11.singh/IdeaProjects/PeriscopeBackend/src/main/java/com/app/services/CustomCodeService.java";
            }


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
