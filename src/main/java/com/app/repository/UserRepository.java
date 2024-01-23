package com.app.repository;


import com.app.models.Project;
import com.app.models.account.User;
import com.app.models.canvas.CanvasData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User save(User user);



}