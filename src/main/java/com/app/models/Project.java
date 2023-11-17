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
}