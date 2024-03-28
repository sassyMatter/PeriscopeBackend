package com.app.repository;

import com.app.models.canvas.CanvasData;
import com.app.models.test.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CanvasRepository extends MongoRepository<CanvasData,String> {

    CanvasData save(CanvasData data);
    List<CanvasData> findAll();

}