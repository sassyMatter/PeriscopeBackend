package com.app.utils;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BashScriptRunner {



    // source of template
    @Value("${}")
    public static String sourceDir;

    // parent directory
    @Value("${}")
    public static String targetParentDir;


    /**
     *
     * @param targetDirName : projectName created from username and email
     */

    public static void createUserProjectDirectory(String targetDirName) {
        try {
            List<String> command = new ArrayList<>();
            command.add("bash");
            command.add("set_up_user_project.sh"); // Name of the Bash script

            // Add command-line arguments
            command.add("-s");
            command.add(sourceDir);
            command.add("-t");
            command.add(targetParentDir);
            command.add("-n");
            command.add(targetDirName);

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.inheritIO(); // Redirect process output to console

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Project creation success.");
            } else {
                System.err.println("Project Creation failed with exit code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }




}
