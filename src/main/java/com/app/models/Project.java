package com.app.models;

import com.app.models.canvas.CanvasData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

// ...

@Document
@Getter
@Setter
public class Project {
    @Id
    private String id;


    @Getter
    private String projectName;

//  For now storing just one image
    private String imageURL;

    private String sourceDir;

    private Configurations configurations;

    private String sourceDirName;

    private boolean isRunning;


    @DBRef
    private CanvasData canvasData;

    // Constructors, getters, and setters


    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getImageURL() {
        return imageURL;
    }
    public CanvasData getCanvasData(){
        return canvasData;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public Configurations getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Configurations configurations) {
        this.configurations = configurations;
    }

    public String getSourceDirName() {
        return sourceDirName;
    }

    public void setSourceDirName(String sourceDirName) {
        this.sourceDirName = sourceDirName;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
    public boolean getRunning(){
        return isRunning;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}