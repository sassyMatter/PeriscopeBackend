package com.app.repository;

import com.app.models.account.Role;
import com.app.models.enums.security.ERoles;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERoles name);
}