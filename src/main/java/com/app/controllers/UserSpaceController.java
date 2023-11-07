package com.app.controllers;


import com.app.models.Project;
import com.app.models.payload.response.MetaDataResponse;
import com.app.models.payload.response.ProjectData;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-space")
@CrossOrigin(origins="*")
@Slf4j
public class UserSpaceController {

    @GetMapping("get-all-projects")
    public ResponseEntity<MetaDataResponse<List<ProjectData>>> getAllProjects(){

        return null;
    }

    @PostMapping("update-project")
    public ResponseEntity<?> updateProject(@RequestBody Project project){

        return null;
    }

    @PostMapping("load-project")
    public ResponseEntity<?> loadProject(@RequestBody ProjectData projectData){

        return null;
    }

    @PostMapping("create-new-project")
    public ResponseEntity<?> createNewProject(@RequestBody ProjectData projectData){

        return null;
    }

    @PostMapping("save-project")
    public ResponseEntity<?> saveProject(@RequestBody Project project){
        // find the user and save project in his name
        return null;
    }



}
