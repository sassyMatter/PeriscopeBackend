package com.app.controllers;


import com.app.models.Project;
import com.app.models.payload.response.ProjectData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@RestController
@RequestMapping("/orchestrate")
@CrossOrigin(origins="*")
@Slf4j
public class OrchestrationController {
    @Value("${com.userSpace.targetParentDirectory}")
    public  String targetParentDir;


    @Value("${docker.compose.executable}")
    private String dockerComposeExecutable;

    private static final Logger logg = LoggerFactory.getLogger(OrchestrationController.class);
    /**
     *
     * Prepare project:
     *    a> Create user directory
     *    b> Move template to user directory
     *    c> Prepare tree nodes
     *    d> Start executing tree
     *    e> For configuration nodes, create initializers and populate them
     *    f> Create Docker Compose file with configuration for available ports
     *    g> Fetch metadata of the container and return it to the user, including endpoints
     *
     * @param project
     * @return
     */

    @PostMapping("run-project")
    public ResponseEntity<?> runProject(@RequestBody Project project) throws IOException, InterruptedException {
//        log.info("project coming {}",project);
        String src=project.getSourceDir();
//        log.info("Source directory {}",src);
//
        String dockerComposeFile=targetParentDir + src+"/src/main/resources/scripts/initializer.sql";
//
//
//        log.info("Directory {}",dockerComposeFile);
//        String tempDir = System.getProperty("java.io.tmpdir");
//        System.out.println("Temporary directory: " + tempDir);
//        Path file = Path.of(dockerComposeFile);
//
//        String dockerfileContent = readDockerfile(dockerComposeFile);
//        Path tempDockerComposePath = writeToTemporaryDockerComposeFile(dockerfileContent);
////        log.info("dockerFileContent {}",dockerfileContent);
////        writeDockerfile(dockerfileContent, dockerpath);
//        bringUpContainers(tempDockerComposePath);
        List<String> command = List.of("cat",dockerComposeFile);
        ProcessBuilder processBuilder = new ProcessBuilder(command);
//        processBuilder.directory(new File(dockerComposeFile));
        // Step 2: Start the process
        Process process = processBuilder.start();

        // Step 3: Read the output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // Wait for the process to complete
        int exitCode = process.waitFor();
        log.info("Exit code{}",exitCode);
        System.out.println("Exit code: " + exitCode);


//        List<String> listFilesCommand = List.of("pwd");
//        ProcessBuilder listFilesProcessBuilder = new ProcessBuilder(listFilesCommand);
//        listFilesProcessBuilder.directory(new File(dockerComposeFile));
//        Process listFilesProcess = listFilesProcessBuilder.start();
//
//        BufferedReader listFilesReader = new BufferedReader(new InputStreamReader(listFilesProcess.getInputStream()));
//        while ((line = listFilesReader.readLine()) != null) {
//            System.out.println(line);
//        }
//
//        int listFilesExitCode = listFilesProcess.waitFor();
//        System.out.println("List files exit code: " + listFilesExitCode);

        return ResponseEntity.ok("Project loaded successfully");
    }
    public String readDockerfile(String sourcePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(sourcePath)));
    }

    public Path writeToTemporaryDockerComposeFile(String content) throws IOException {
        Path tempFile = Files.createTempFile("docker-compose-", ".yml");
        Files.write(tempFile, content.getBytes());
        log.info("tempfile {}",tempFile);
        return tempFile;
    }
    public void bringUpContainers(Path dockerComposePath) throws IOException {
        System.out.println("Docker Compose file path: " + dockerComposePath.toString());
        String destPath=dockerComposePath.toString();
        log.info("destPath{}",destPath);
        String destContent=readDockerfile(destPath);
        log.info("content {}",destContent);


        if (!Files.exists(dockerComposePath)) {
            System.out.println("Docker Compose file does not exist at the specified path.");
        }
        else{
            log.info("dockercomposeexecutable {}",dockerComposeExecutable);
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(dockerComposeExecutable,"-f", destPath, "up", "-d");


                Process process = processBuilder.start();

                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Docker Compose file exists at the specified path.");
        }


    }


}
