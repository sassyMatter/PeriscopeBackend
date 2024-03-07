package com.app.services;


import com.app.utils.ScriptLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScriptService {


    @Autowired
    private ScriptLoader scriptLoader;
    @Value("${com.userSpace.templateDirectory}")
    public String sourceDir;

    // parent directory
    @Value("${com.userSpace.targetParentDirectory}")
    public  String targetParentDir;



    public File getScriptFile(String scriptName) throws IOException {
        log.info("msg coming");
        try {
            log.info("test before");
            File file=scriptLoader.loadScriptFile(scriptName);
            log.info("file {}",file);
            return file;
        }
        catch(Error e){
            log.info("error is",e);
        }
        return null;

    }



    public int createUserProjectDirectory(String targetDirName) {
        try {
            List<String> command = new ArrayList<>();
            log.info("command");

            File script = getScriptFile("set_up_user_project.sh");
            log.info("running");
            log.info(script.getPath());
            command.add("bash");

            command.add(script.getPath()); // Name of the Bash script

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
            log.info("process, {}",process);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Project Template creation success.");
                return 1;
            } else {
                System.err.println("Project Template Creation failed with exit code " + exitCode);
                return 0;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * delete project on any update and recreate it with requirements
     */
    public int deleteUserProjectDirectory(String targetParentDir){
        targetParentDir="/user-space-directory/"+targetParentDir;
        log.info("targetParentDir");
        try {
            List<String> command = new ArrayList<>();
            File script = getScriptFile("delete_user_directory.sh");

            log.info(script.getPath());
            command.add("bash");
            command.add(script.getPath()); // Name of the Bash script

            // Add command-line arguments
            command.add("-d");
            command.add(targetParentDir);


            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.inheritIO(); // Redirect process output to console

            Process process = processBuilder.start();
            log.info("process {}",process);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Deleted User Project creation success.");
                return 1;
            } else {
                System.err.println("Could not deleted user project " + exitCode);
                return 0;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }



}
