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
    public MetaDataResponse<List<ProjectData>> getAllProjects() {
        List<Project> allprojectsData = projectService.getALlProjects();
        List<ProjectData> projectData = projectMapper.mapToProjectDataList(allprojectsData);


        return MetaDataResponse.<List<ProjectData>>
                        builder()
                        .data(projectData)
                        .httpStatus(HttpStatus.OK)
                .messageCode("SUCCESS")
                .build();
    }

    @PostMapping("/update-project")
    public ResponseEntity<?> updateProject(@RequestBody Project project) {
        Optional<Project> existingProject = projectRepository.findById(project.getId());
        if (existingProject.isPresent()) {
            // Update project properties
            Project updatedProject = existingProject.get();
            updatedProject.setProjectName(project.getProjectName());
            updatedProject.setImageURL(project.getImageURL());
            updatedProject.setConfigurations(project.getConfigurations());
            // Update other properties as needed

            projectRepository.save(updatedProject);
            return ResponseEntity.ok("Project updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/load-project")
    public ResponseEntity<?> loadProject(@RequestBody ProjectData projectData) {
        // Load project based on provided ProjectData
        // Implement logic to load project from the database
        return ResponseEntity.ok("Project loaded successfully");
    }

    @PostMapping("/create-new-project")
    public MetaDataResponse<Project> createNewProject(@RequestBody ProjectData projectData) {
        // Create a new project and save it to the database
        Project project = projectMapper.mapProjectDataToProject(projectData);
        // find user Id from userDetails service

        String userName = userDetailsService.getCurrentUsername();
        Project savedProject = projectService.createProjectForUser(userName, project);

        return MetaDataResponse.<Project>
                        builder()
                .data(savedProject)
                .httpStatus(HttpStatus.OK)
                .messageCode("SUCCESS")
                .build();
    }

    @PostMapping("/save-project")
    public MetaDataResponse<String> saveProject(@RequestBody Project project) {

        // Save the project to the database
        projectRepository.save(project);

        return MetaDataResponse.<String>
                        builder()
                .data("Project Saved Successfully")
                .httpStatus(HttpStatus.OK)
                .messageCode("SUCCESS")
                .build();
    }



}
