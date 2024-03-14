package com.app.controllers;

import com.app.models.Response;
import com.app.models.account.User;
import com.app.models.Project;
import com.app.models.canvas.CanvasData;
import com.app.models.canvasSchema.TreeNode;
import com.app.models.payload.response.MetaDataResponse;
import com.app.models.payload.response.ProjectData;


import com.app.repository.ProjectRepository;
import com.app.repository.UserRepository;
import com.app.services.core.ProjectService;
import com.app.services.core.TreeBuilderService;
import com.app.services.core.UserDetailsServiceImpl;
import com.app.utils.ProjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
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

    @Autowired
    TreeBuilderService treeBuilderService;

    @Value("${com.userSpace.targetParentDirectory}")
    public  String targetParentDir;

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
    public MetaDataResponse<Project> runProject(@RequestBody String projectName) {
        // Load project based on provided ProjectData
        String username=userDetailsService.getCurrentUsername();
        List<Project> userProjects = userDetailsService.getUserProjects(username);
        log.info("username {}",username);
        Project project=null;
        for(Project project1:userProjects){
            if(project1.getProjectName().equals(projectName))
                project=project1;
        }
        if(project!=null){
            projectRepository.deleteById(project.getId());
            boolean value=project.getRunning();
            value=!value;
            project.setRunning(value);
            projectRepository.save(project);
        }


        log.info("running project");
        // Implement logic to load project from the database
//        return ResponseEntity.ok("Project loaded successfully");
        // return projectService.loadProject(projectData);
        return MetaDataResponse.<Project>
                        builder()
                .data(project)
                .httpStatus(HttpStatus.OK)
                .messageCode("Project run successfully for User")
                .build();
    }


//    @PostMapping("/build-project")
//    public ResponseEntity<?> buildProject(@RequestBody Project project) {
//        // Load project based on provided ProjectData
//
//        // Implement logic to load project from the database
//        return ResponseEntity.ok("Project built successfully");
//        // return projectService.loadProject(projectData);
//    }


    // redundant

    @PostMapping("/create-project")
    public MetaDataResponse<Project> createOrUpdateProject(@RequestBody Project project) {

//        Project project = projectMapper.mapProjectDataToProject(projectData);

        String userName = userDetailsService.getCurrentUsername();
        Project updatedProject = null;
        log.info("UserName {}", userName);
        Project existingProject = null;
        Optional<String> projectName=Optional.ofNullable(project.getProjectName());
        if(projectName.isPresent()){
            existingProject = projectService.findProjectNameAndUser(userName, project.getProjectName());
        }



        log.info("Existing project:: {} ", existingProject);
        if (existingProject != null) {
            log.info("Project with this name already exists");
            return MetaDataResponse.<Project>
                            builder()
                    .data(null)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .messageCode("Project name already exists,it should be unique")
                    .build();
//           log.info("deleted");
       }

            // If the project doesn't exist, create and save a new project
//            log.info("projects ,{}",project);
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
                .data(null)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .messageCode("Project not valid")
                .build();
    }
    @PostMapping("/update-project")
    public MetaDataResponse<Project> updateProject(@RequestBody Project project){
        String userName = userDetailsService.getCurrentUsername();
//        log.info("username{},",userName);
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
        Optional<String> projectName = Optional.ofNullable(project.getProjectName());
        log.info("projectId {}",projectName);
        if(projectName.isPresent()){
            existingProject = projectService.findProjectNameAndUser(userName, project.getProjectName());
        }
        log.info("existing project {}",existingProject);
        if(existingProject!=null) {
            // project exists
            log.info("project deleted");
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
//    @PostMapping("/run-project-test")
//    public MetaDataResponse<Project> RunProjectTest(@RequestBody Project project){
//        log.info("project info {}",project);
//        log.info("Starting simulation with project {} ", project);
//        CanvasData canvasData  = project.getCanvasData();
//        String projectDir = project.getSourceDir();
//        projectService.createDeleteDirectory(project);
//
//        log.info("Sources directory is {} ", projectDir);
//        log.info("Parent directory is {} ", targetParentDir);
//
//
//        TreeNode root = treeBuilderService.buildTree(canvasData);
//
//        treeBuilderService.processGraph(root, projectDir);
//        String message="project";
//
////        return null;
//
//        return MetaDataResponse.<Project>
//                        builder()
//                .data(null)
//                .httpStatus(HttpStatus.OK)
//                .messageCode("Project built successfully for User")
//                .build();
//    }

    @PostMapping("/build-project-test")
    public MetaDataResponse<Project> buildProjectTest(@RequestBody String projectname){
        log.info("project info {}",projectname);
        log.info("Starting simulation with project {} ", projectname);
        String username=userDetailsService.getCurrentUsername();
        List<Project> userProjects = userDetailsService.getUserProjects(username);

        Project project=null;
        for(Project project1:userProjects){
            if(project1.getProjectName().equals(projectname))
                project=project1;

        }
        if(project!=null) {
//            log.info("Project name {} ,project{}",projectname,project);
            CanvasData canvasData = project.getCanvasData();
            String projectDir = project.getSourceDir();
            projectService.createDeleteDirectory(project);

            log.info("Sources directory is {} ", projectDir);
            log.info("Parent directory is {} ", targetParentDir);


            TreeNode root = treeBuilderService.buildTree(canvasData);

            treeBuilderService.processGraph(root, projectDir);
            String message = "project";
            return MetaDataResponse.<Project>
                            builder()
                    .data(project)
                    .httpStatus(HttpStatus.OK)
                    .messageCode("Project built successfully for User")
                    .build();
        }

        return MetaDataResponse.<Project>
                        builder()
                .data(null)
                .httpStatus(HttpStatus.OK)
                .messageCode("Project does not exist User")
                .build();


    }
    @GetMapping("/running-project")
    public MetaDataResponse<List<Project>> runningProjects() {
        String currUsername = userDetailsService.getCurrentUsername();
//        List<ProjectData> projectData = projectMapper.mapToProjectDataList(allprojectsData);
        // String userName = userDetailsService.getCurrentUsername();
        log.info("running");
        List<Project> userProjects = userDetailsService.getUserProjects(currUsername);
        List<Project>runningProjects=new ArrayList<>();
        for(Project pro:userProjects){
            if(pro.isRunning()){
                runningProjects.add(pro);
            }
        }
        return MetaDataResponse.<List<Project>>
                        builder()
                .data(runningProjects)
                .httpStatus(HttpStatus.OK)
                .messageCode("SUCCESS")
                .build();
    }


}
