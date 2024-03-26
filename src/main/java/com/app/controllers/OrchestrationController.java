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




        return ResponseEntity.ok("Project loaded successfully");
    }



}
