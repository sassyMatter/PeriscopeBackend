package com.app.controllers;


import com.app.models.payload.response.ProjectData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orchest")
@CrossOrigin(origins="*")
@Slf4j
public class OrchestrationController {

    @PostMapping("run-simulation")
    public ResponseEntity<?> runSimulation(@RequestBody ProjectData projectData){

        return null;
    }
}
