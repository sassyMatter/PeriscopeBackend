package com.app.models;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Configurations {

    public String memory;
    public String cpus;
    public String storage;

}
