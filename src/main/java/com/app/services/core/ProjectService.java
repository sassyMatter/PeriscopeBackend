package com.app.services.core;

import com.app.models.Project;
import com.app.models.account.User;
import com.app.models.canvas.CanvasData;
import com.app.repository.CanvasRepository;
import com.app.repository.ProjectRepository;
import com.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProjectService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CanvasRepository canvasDataRepository;


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


    public List<Project> getALlProjects(){
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
     * @param projectId
     */
    // Delete a project
    public void deleteProject(String projectId) {
        // find user and User object
         Project project=projectRepository.findById(projectId).orElse(null);
        
        // delete project reference and
        // finally delete the project
        projectRepository.deleteById(projectId);
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


    public Project createProjectForUser(String userId, Project project) {
        log.info("Creating Project for User with user Id:: {} ", userId);
        User user = userRepository.findByUsername(userId).orElse(null);
        if (user != null) {
            // Generate a predictable yet unique project name (e.g., appending timestamp)
//            String projectName = generateUniqueProjectName(user.getUsername(), project.getProjectName());

            // Set other properties as needed
//          project.setCanvasData(new CanvasData());
            Project savedProject =  projectRepository.save(project);
            // Add the project to the user's list of projects
            user.getProjects().add(savedProject);
//            userRepository.save(user);

            // Save the project
            return savedProject;
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

    public Project updateProjectState(String userName, String projectId, Project newProjectState) {
        // Find the user
        Optional<User> user = userRepository.findByUsername(userName);
        if(!user.isPresent()){
            // raise exception and catch it to throw error
        }
//        Optional<User> optionalUser = userRepository.findById(user.get().getId());
        if (user.isPresent()) {
            User existingUser = user.get();
            // Find the project within the user's projects
            Optional<Project> optionalProject = existingUser.getProjects().stream()
                    .filter(project -> project.getId().equals(projectId))
                    .findFirst();

            if (optionalProject.isPresent()) {
                Project existingProject = optionalProject.get();

                // Update the properties of the existing project with the new state
                existingProject.setProjectName(newProjectState.getProjectName());
                existingProject.setImageURL(newProjectState.getImageURL());
                existingProject.setConfigurations(newProjectState.getConfigurations());
                existingProject.setCanvasData(newProjectState.getCanvasData()); // Replace the entire CanvasData

                // Update other properties as needed

                // Save the updated project
              return  projectRepository.save(existingProject);
            }
        }
        return null;
    }
}
