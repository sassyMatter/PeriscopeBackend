package com.app.controllers;


import com.app.models.Project;
import com.app.models.payload.response.MetaDataResponse;
import com.app.models.payload.response.ProjectData;


import com.app.repository.ProjectRepository;
import com.app.services.core.ProjectService;
import com.app.services.core.UserDetailsServiceImpl;
import com.app.utils.ProjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-space")
@CrossOrigin(origins="*")
@Slf4j
public class UserSpaceController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMapper projectMapper;




    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/get-all-projects")
    public MetaDataResponse<List<Project>> getAllProjects() {
        List<Project> allprojectsData = projectService.getALlProjects();
//        List<ProjectData> projectData = projectMapper.mapToProjectDataList(allprojectsData);


        return MetaDataResponse.<List<Project>>
                        builder()
                        .data(allprojectsData)
                        .httpStatus(HttpStatus.OK)
                .messageCode("SUCCESS")
                .build();
    }


    @PostMapping("/load-project")
    public ResponseEntity<?> loadProject(@RequestBody ProjectData projectData) {
        // Load project based on provided ProjectData
       
        // Implement logic to load project from the database
        return ResponseEntity.ok("Project loaded successfully");
        // return projectService.loadProject(projectData);
    }

    // redundant
   
    @PostMapping("/create-update-project")
    public MetaDataResponse<Project> createOrUpdateProject(@RequestBody Project project) {
        // Create a new project or update an existing project based on the project name
//        Project project = projectMapper.mapProjectDataToProject(projectData);

        String userName = userDetailsService.getCurrentUsername();
        Project updatedProject = null;
        log.info("UserName :: {} ", userName);


        Project existingProject = null;
        Optional<String> projectId = Optional.ofNullable(project.getId());
        if(projectId.isPresent()){
            existingProject = projectService.findProjectIdAndUser(userName, project.getId());
        }

        log.info("Existing project:: {} ", existingProject);
        if (existingProject != null) {
            // If the project already exists, update its state
            log.info("Updating the project ");
          updatedProject =   projectService.updateProjectState(userName, existingProject.getId(), project);
        } else {
            // If the project doesn't exist, create and save a new project
            Project savedProject = projectService.createProjectForUser(userName, project);
            // put null check for saved project
            return MetaDataResponse.<Project>
                            builder()
                    .data(savedProject)
                    .httpStatus(HttpStatus.OK)
                    .messageCode("Created Project for User")
                    .build();
        }

        return MetaDataResponse.<Project>
                        builder()
                .data(updatedProject)
                .httpStatus(HttpStatus.OK)
                .messageCode("Updated Project State")
                .build();
    }



}
