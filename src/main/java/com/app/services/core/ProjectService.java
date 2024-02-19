package com.app.services.core;

import com.app.models.Project;
import com.app.models.account.User;
import com.app.models.canvas.CanvasData;
import com.app.repository.CanvasRepository;
import com.app.repository.ProjectRepository;
import com.app.repository.UserRepository;
import com.app.services.ScriptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ProjectService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CanvasRepository canvasDataRepository;
    @Autowired
    ScriptService scriptService;

    /**
     *
     * @param userId
     * @param project
     * @return
     */

    // Create a new project for a user
//    public Project createProjectForUser(String userId, Project project) {
//        User user = userRepository.findById(userId).orElse(null);
//        if (user != null) {
//            // save canvas data
//
//            project.setCanvasData(new CanvasData());
//            user.getProjects().add(project);
//            // Initialize with a new canvasData
//            return projectRepository.save(project);
//        }
//        return null;
//    }


    /**
     *
     * @param updatedProject
     * @return
     */

    // Update a project, including canvasData and other properties
    public Project updateProject(Project updatedProject) {
        // Find the existing project in the database by its ID, id could be null
        // place null check
        Project existingProject = projectRepository.findById(updatedProject.getId()).orElse(null);

        if (existingProject != null) {
            // Update project properties
            existingProject.setProjectName(updatedProject.getProjectName());
//            existingProject.setDescription(updatedProject.getDescription());
            // Update other project properties as needed

            // Update canvasData
            CanvasData updatedCanvasData = updatedProject.getCanvasData();
            if (updatedCanvasData != null) {
                CanvasData existingCanvasData = existingProject.getCanvasData();
                if (existingCanvasData != null) {
                    // If there was no existing canvasData, set the new one
                    existingProject.setCanvasData(updatedCanvasData);
                }
            }

            // Save the updated project to the database
            return projectRepository.save(existingProject);
        }
        return null;
    }


    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    /**
     *
     * @param projectId
     * @return
     */

    // Read a project by ID
    public Project getProjectById(String projectId) {

        return projectRepository.findById(projectId).orElse(null);
    }

    /**
     *
//     * @param projectId
     */
    // Delete a project
    public void deleteProject(String username,Project project) {
        // find user and User object
        User user = userRepository.findByUsername(username).orElse(null);
//         Project project=projectRepository.findById(projectId).orElse(null);
        
        // delete project reference and
        assert user != null;
        if(user.getProjects()!=null) {
            log.info("users project before deleting,{}",user.getProjects());
        }
        if(user.getProjects()!=null) {
            log.info("delete ongoing");

        }


        //saved it after deleting the reference from the user


        // finally delete the project
        if(user.getProjects()!=null) {

//            log.info("users project after deleting,{}", user.getProjects());
//            for(Project projectx: user.getProjects()){
//                log.info("projectx {} ",projectx.getId());
//            }
            boolean removed=false;
            Project deleted=null;
            for(Project pro: user.getProjects()) {
                if (Objects.equals(pro.getId(), project.getId())) {
                    deleted = pro;
                    removed=true;
                }
            }

            log.info("project {}",project.getId());
            if( removed ) {
                String sourcedir="/user-space-directory/"+project.getSourceDir();
                log.info("getting project source dir,{}",sourcedir);
//                log.info("sourcedir {}",project.getSourceDir());
                int x=scriptService.deleteUserProjectDirectory(sourcedir);
                if(x==1){
                    log.info("project deleted from directory");
                }
                else{
                    log.info("project not deleted from directory");
                }
                projectRepository.deleteById(project.getId());
                user.getProjects().remove(deleted);
                log.info("project removed");
                userRepository.save(user);
            }
            else{
                log.info("not working");
            }
//            for(Project projecty: user.getProjects()){
//                log.info("projecty {} ",projecty.getId());
//            }
        }

    }

    /**
     *
     * @param canvasData
     * @return
     */
    // Create canvasData
    public CanvasData createCanvasData(CanvasData canvasData) {

        return canvasDataRepository.save(canvasData);
    }

    // Read canvasData by ID
    public CanvasData getCanvasDataById(String canvasDataId) {
        return canvasDataRepository.findById(canvasDataId).orElse(null);
    }

    // Update canvasData
    public CanvasData updateCanvasData(CanvasData canvasData) {

        return canvasDataRepository.save(canvasData);
    }

    // Delete canvasData
    public void deleteCanvasData(String canvasDataId) {

        canvasDataRepository.deleteById(canvasDataId);
    }


    public Project createProjectForUser(String username, Project project) {
        log.info("Creating Project for User with user Id:: {} ", username);
        User user = userRepository.findByUsername(username).orElse(null);
        log.info("user {}",user);
        if (user != null) {
            // Generate a predictable yet unique project name (e.g., appending timestamp)
//            String projectName = generateUniqueProjectName(user.getUsername(), project.getProjectName());

            // Set other properties as needed

            String dirName = username + "-" + project.getProjectName();
            log.info("sourcedir:{}", dirName);
            project.setSourceDir(dirName);
            int x=scriptService.createUserProjectDirectory(dirName);
            System.out.println(x);
            if(x==1){
                log.info("directory created");
            }
            else{
                log.info("directory not created");
            }
//          project.setCanvasData(new CanvasData());
            //project repo not implemented
            if(project.getCanvasData() != null){
                canvasDataRepository.save(project.getCanvasData());
            }
            
            project.setSourceDirName(project.getProjectName());

            log.info("last");
            Project savedProject =  projectRepository.save(project);

            try{
                log.info("my projects:::: {}", user.getProjects() );

                user.getProjects().add(savedProject);
                log.info("my projects:::: {}", user.getProjects() );
                userRepository.save(user);
                // Add the project to the user's list of projects

            }
            catch (Error error){
                log.info("error ::::::",error);
            }finally {
                log.info(savedProject.toString());
                return savedProject;
            }


//            log.info("savedProject12  {}",savedProject);

//
//

//            log.info("saveddone");
            // Save the project
//            return savedProject;
        }
        return null;
    }

    private String generateUniqueProjectName(String username, String originalName) {
        // Use a combination of the username, original name, and timestamp to create a unique name
        String timestamp = String.valueOf(System.currentTimeMillis());
        return username + "_" + originalName + "_" + timestamp;
    }


    public Project findProjectIdAndUser(String username, String Id) {

        User user = userRepository.findByUsername(username).orElse(null);
        log.info("User is {} ", user);
        if (user != null) {

            return user.getProjects()
                    .stream()
                    .filter(project -> project.getId().equals(Id))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
    // changes made by me
    public Project findProjectNameAndUser(String username,String projectName){
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            log.info("User is {} ", user.getId());

            Set<Project> project1=user.getProjects();
            log.info("set of projects,{}", project1);
            if(user.getProjects().isEmpty())
                return null;
            return user.getProjects()
                    .stream()
                    .filter(project -> project.getProjectName().equals(projectName))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public Project updateProjectState(String userName, String projectName, Project newProjectState) {
        // Find the user
        Optional<User> user = userRepository.findByUsername(userName);
        log.info("user is present {}:", user.isPresent());
        if(!user.isPresent()){
            // raise exception and catch it to throw error
        }
        if(newProjectState.getCanvasData()!=null)
        canvasDataRepository.save(newProjectState.getCanvasData());
//        Optional<User> optionalUser = userRepository.findById(user.get().getId());
        if (user.isPresent()) {
            log.info("user {}",user);
            log.info("projectName {}",newProjectState);
            User existingUser = user.get();
            // Find the project within the user's projects
            Optional<Project> optionalProject = existingUser.getProjects().stream()
                    .filter(project -> project.getProjectName().equals(projectName))
                    .findFirst();

            if (optionalProject.isPresent()) {
                Project existingProject = optionalProject.get();

                // Update the properties of the existing project with the new state
               // existingProject.setProjectName(newProjectState.getProjectName());
                existingProject.setImageURL(newProjectState.getImageURL());
                existingProject.setConfigurations(newProjectState.getConfigurations());
                if(newProjectState.getCanvasData()!=null)
                existingProject.setCanvasData(newProjectState.getCanvasData()); // Replace the entire CanvasData
//                existingProject.setConfigurations(newProjectState.getConfigurations());
                // Update other properties as needed

                // Save the updated project
              return  projectRepository.save(existingProject);
            }
        }
        return null;
    }
}
