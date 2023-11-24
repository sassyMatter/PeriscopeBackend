package com.app.controllers;


import com.app.models.Project;
import com.app.models.payload.response.ProjectData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orchestrate")
@CrossOrigin(origins="*")
@Slf4j
public class OrchestrationController {

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
    public ResponseEntity<?> runProject(@RequestBody Project project){

        return null;
    }


}
