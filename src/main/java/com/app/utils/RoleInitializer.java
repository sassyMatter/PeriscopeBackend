package com.app.utils;

import com.app.models.account.Role;
import com.app.models.enums.security.ERoles;
import com.app.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@Slf4j
public class RoleInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        log.info("Initializing available roles");
        // Check if ROLE_USER exists
        Optional<Role> roleUser = roleRepository.findByName(ERoles.ROLE_USER);
        if (roleUser.isEmpty()) {
            roleRepository.save(new Role(ERoles.ROLE_USER));
        }

        // Check if ROLE_ADMIN exists
        Optional<Role> roleAdmin = roleRepository.findByName(ERoles.ROLE_ADMIN);
        if (roleAdmin.isEmpty()) {
            roleRepository.save(new Role(ERoles.ROLE_ADMIN));
        }
    }
}
