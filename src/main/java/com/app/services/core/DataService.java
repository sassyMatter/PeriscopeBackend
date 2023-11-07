package com.app.services.core;

import com.app.models.Project;
import com.app.models.account.User;
import com.app.models.canvas.CanvasData;
import com.app.repository.CanvasRepository;
import com.app.repository.ProjectRepository;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {
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
    public Project createProjectForUser(String userId, Project project) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            // save canvas data

            project.setCanvasData(new CanvasData());
            user.getProjects().add(project);
            // Initialize with a new canvasData
            return projectRepository.save(project);
        }
        return null;
    }


    /**
     *
     * @param updatedProject
     * @return
     */

    // Update a project, including canvasData and other properties
    public Project updateProject(Project updatedProject) {
        // Find the existing project in the database by its ID
        Project existingProject = projectRepository.findById(updatedProject.getId()).orElse(null);

        if (existingProject != null) {
            // Update project properties
            existingProject.setName(updatedProject.getName());
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
}
