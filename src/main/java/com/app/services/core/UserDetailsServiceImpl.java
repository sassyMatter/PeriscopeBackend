package com.app.services.core;


import com.app.models.Project;
import com.app.models.account.User;
import com.app.repository.CanvasRepository;
import com.app.repository.ProjectRepository;
import com.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CanvasRepository canvasDataRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Find user in database..");
        UserDetails user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        log.info("Valid account:: {} ", user.isAccountNonExpired());
        return user;
    }


    // in multi-threaded environment me might face issue with Security Context holder , do we?
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "007";
        }
        return authentication.getName();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public List<Project> getUserProjects(String username){
        
        User user = userRepository.findByUsername(username).orElse(null);
        List<Project> temp = new ArrayList<>();
        if(user != null){
            
            user.getProjects().stream().forEach(proj -> temp.add(proj));
        }
        return temp;
    }
}
