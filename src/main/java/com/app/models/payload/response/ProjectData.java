package com.app.models.payload.response;


import com.app.models.Configurations;
import com.app.models.enums.ConfigurationNodes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectData {

    public String id;

    public String projectName;

    public String imageUrl;

    public Configurations configurations;

    public Boolean isRunning;
}
