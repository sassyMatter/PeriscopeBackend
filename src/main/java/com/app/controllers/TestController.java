package com.app.controllers;



import com.app.models.Project;
import com.app.models.Response;
import com.app.producers.KafkaProducer;
import com.app.services.ScriptService;
import com.app.services.core.UserDetailsServiceImpl;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins="*")
@RequestMapping("/hello" )
@Slf4j
public class TestController {

    @Autowired
    KafkaProducer testProducer;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    ScriptService scriptService;

    @Autowired
    Gson gson;

    @GetMapping
    public Response Sample(){
        log.info("Hit");
        Response response = new Response();
        response.setResponse("Hello, Connected to server New");

//        testProducer.sendMessage("testTopic", gson.toJson(response));
        return response;
    }

    // create a user project with username from principle and copy template directory
    // and start writing to it. Environment variables to the .env file.
    // run some scripts on the template and get it up.

    @PostMapping("/testing-template")
    public Response testTemplate(@RequestBody Project project){

        String userName = userDetailsService.getCurrentUsername();
        log.info("creating project {} for userName: {} ", project.getProjectName(), userName);

        // creating a random project dir name using project counts of user and projectName,
        // for now using only projectName
        String dirName = userName + "-" + project.getProjectName();
        scriptService.createUserProjectDirectory(dirName);





        return null;
    }







}
