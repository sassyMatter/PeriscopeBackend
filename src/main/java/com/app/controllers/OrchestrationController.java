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

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;

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
    @Value("${com.userSpace.templateDirectory}")
    public String sourceDir;
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
    public ResponseEntity<?> runProject(@RequestBody Project project) throws IOException {
        log.info("project coming {}",project);
        String src=project.getSourceDir();
        log.info("Source directory {}",src);

        String dockerComposeFile=targetParentDir + src+"/docker-compose.yml";
        String dockerpath="../../../docker-compose2.yml";
        log.info("Directory {}",dockerComposeFile);
//        try (BufferedReader reader = new BufferedReader(new FileReader(dockerComposeFile))) {
//            StringBuilder content = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content.append(line).append(System.lineSeparator());
//            }
//            log.info("File content: {}",
//                    content.toString());
//        } catch (IOException e) {
//            log.error("Error reading file: {}", e.getMessage());
//        }
//        String destpath="../../../docker-compose2.yml";
        Path file = Path.of(dockerComposeFile);
//        log.info("file:: {} ", file);
        String dockerfileContent = readDockerfile(dockerComposeFile);
//        log.info("dockerFileContent {}",dockerfileContent);
        writeDockerfile(dockerfileContent, dockerpath);
        try {
            // Build the Docker image
            ProcessBuilder buildProcess = new ProcessBuilder("docker", "build", "-t", "my-image", "-f", dockerpath, ".");
            Process build = buildProcess.start();
            build.waitFor();

            // Run the Docker container
            ProcessBuilder runProcess = new ProcessBuilder("docker", "run", "-d", "my-image");
            Process run = runProcess.start();
            run.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Project loaded successfully");
    }
    public String readDockerfile(String sourcePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(sourcePath)));
    }
    public void writeDockerfile(String content, String destinationPath) throws IOException {
        Files.write(Paths.get(destinationPath), content.getBytes());
        log.info(destinationPath);
    }

}
