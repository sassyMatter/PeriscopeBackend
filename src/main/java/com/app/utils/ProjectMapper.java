package com.app.utils;

import com.app.models.Configurations;
import com.app.models.Project;
import com.app.models.payload.response.ProjectData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ProjectMapper {

    public List<ProjectData> mapToProjectDataList(List<Project> projects) {
        return projects.stream()
                .map(ProjectMapper::mapToProjectData)
                .collect(Collectors.toList());
    }

    public static ProjectData mapToProjectData(Project project) {
        ProjectData projectData = new ProjectData();
        project.setId(project.getId());
        projectData.setProjectName(project.getProjectName());
        projectData.setImageUrl(project.getImageURL());
        projectData.setConfigurations(project.getConfigurations());
        projectData.setIsRunning(false);
        return projectData;
    }

    public Project mapProjectDataToProject(ProjectData projectData){
        Project project = new Project();
        project.setId(projectData.getId());
        project.setProjectName(projectData.getProjectName());
        project.setConfigurations(projectData.getConfigurations());
        return project;
    }

}

