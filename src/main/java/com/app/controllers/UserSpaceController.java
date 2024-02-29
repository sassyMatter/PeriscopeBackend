package com.app.controllers;

import com.app.models.account.User;
import com.app.models.Project;
import com.app.models.payload.response.MetaDataResponse;
import com.app.models.payload.response.ProjectData;


import com.app.repository.ProjectRepository;
import com.app.repository.UserRepository;
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
public class UserSpaceController{

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMapper projectMapper;


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;



    @GetMapping("/get-all-projects")
    public MetaDataResponse<List<Project>> getAllProjects() {
        String currUsername = userDetailsService.getCurrentUsername();
//        List<ProjectData> projectData = projectMapper.mapToProjectDataList(allprojectsData);
        // String userName = userDetailsService.getCurrentUsername();
        
        List<Project> userProjects = userDetailsService.getUserProjects(currUsername);
        return MetaDataResponse.<List<Project>>
                        builder()
                        .data(userProjects)
                        .httpStatus(HttpStatus.OK)
                .messageCode("SUCCESS")
                .build();
    }


    @PostMapping("/run-project")
    public ResponseEntity<?> runProject(@RequestBody ProjectData projectData) {
        // Load project based on provided ProjectData
       
        // Implement logic to load project from the database
        return ResponseEntity.ok("Project loaded successfully");
        // return projectService.loadProject(projectData);
    }


    @PostMapping("/build-project")
    public ResponseEntity<?> buildProject(@RequestBody Project project) {
        // Load project based on provided ProjectData

        // Implement logic to load project from the database
        return ResponseEntity.ok("Project built successfully");
        // return projectService.loadProject(projectData);
    }


    // redundant

    @PostMapping("/create-project")
    public MetaDataResponse<Project> createOrUpdateProject(@RequestBody Project project) {

//        Project project = projectMapper.mapProjectDataToProject(projectData);

        String userName = userDetailsService.getCurrentUsername();
        Project updatedProject = null;
        log.info("UserName {}", userName);


        Project existingProject = null;
//        Optional<String> projectId = Optional.ofNullable(project.getId());
        Optional<String> projectName=Optional.ofNullable(project.getProjectName());
        if(projectName.isPresent()){
            existingProject = projectService.findProjectNameAndUser(userName, project.getProjectName());
//            log.info("Existing project_working:: {} ", existingProject);
        }
//
//
        log.info("Existing project:: {} ", existingProject);
        if (existingProject != null) {
            // If the project already exists, update its state
//            log.info("Updating the project ");

           log.info("deleting the project");
           projectService.deleteProject(userName,existingProject);

           log.info("deleted");
       }

            // If the project doesn't exist, create and save a new project
            log.info("projects ,{}",project);
            Project savedProject = projectService.createProjectForUser(userName, project);
            // put null check for saved project

            log.info("savedProject {}",savedProject);

            if(savedProject != null)
                return MetaDataResponse.<Project>
                            builder()
                    .data(savedProject)
                    .httpStatus(HttpStatus.OK)
                    .messageCode("Created Project for User")
                    .build();


        return MetaDataResponse.<Project>
                        builder()
                .data(updatedProject)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .messageCode("Project not valid")
                .build();
    }
    @PostMapping("/update-project")
    public MetaDataResponse<Project> updateProject(@RequestBody Project project){
        String userName = userDetailsService.getCurrentUsername();
        log.info("username{},",userName);
        Project savedproject=projectService.updateProjectState(userName,project.getProjectName(),project);
        if(savedproject!=null) {
            return MetaDataResponse.<Project>
                            builder()
                    .data(savedproject)
                    .httpStatus(HttpStatus.OK)
                    .messageCode("Project updated")
                    .build();
        }
        return null;
    }
    @PostMapping("/delete-project")
    public  MetaDataResponse<Project> delete_directory(@RequestBody Project project){
        log.info("deletion going");
        String userName = userDetailsService.getCurrentUsername();

        Project existingProject = null;
        Optional<String> projectId = Optional.ofNullable(project.getProjectName());
        log.info("projectId {}",projectId);
        if(projectId.isPresent()){
            existingProject = projectService.findProjectNameAndUser(userName, project.getProjectName());
        }
        log.info("existing project {}",existingProject);
        if(existingProject!=null) {
            // project exists
            log.info("project deltee");
            projectService.deleteProject(userName,existingProject);

            return MetaDataResponse.<Project>
                            builder()
                    .data(project)
                    .httpStatus(HttpStatus.OK)
                    .messageCode("Deleted Project State")
                    .build();

        }
        else {
            return MetaDataResponse.<Project>
                            builder()
                    .data(project)
                    .httpStatus(HttpStatus.OK)
                    .messageCode("Project does not exist")
                    .build();
        }

    }


}
