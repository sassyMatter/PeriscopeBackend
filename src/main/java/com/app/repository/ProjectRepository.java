package com.app.repository;

import com.app.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.*;

public interface ProjectRepository extends MongoRepository<Project,String> {

    Project save(Project projectData);

    List<Project> findAll();

}
