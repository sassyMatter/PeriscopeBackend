package com.app.models;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RunningConfigurations {

    public String url;
    public Boolean isRunning;

}
