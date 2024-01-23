package com.app.controllers;



import com.app.models.Project;
import com.app.models.Response;
import com.app.models.payload.response.MetaDataResponse;
import com.app.producers.KafkaProducer;
import com.app.services.ScriptService;
import com.app.services.core.UserDetailsServiceImpl;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/auth" )
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


    /**
     * Method to test creation of templates
     * @param project
     * @return
     */

    @PostMapping("/testing-template")
    public MetaDataResponse testTemplate(@RequestBody Project project){

        String userName = userDetailsService.getCurrentUsername();
        log.info("creating project {} for userName: {} ", project.getProjectName(), userName);

        // creating a random project dir name using project counts of user and projectName,
        // for now using only projectName
        String dirName = userName + "-" + project.getProjectName();
        int result = scriptService.createUserProjectDirectory(dirName);

        if (result == 1) {
//        Response response = new Response();
            MetaDataResponse response = MetaDataResponse
                    .builder()
                    .httpStatus(HttpStatus.OK)
                    .messageCode("Created Project Template for the User")
                    .build();
            return response;
        }
        return MetaDataResponse
                .builder()
                .httpStatus(HttpStatus.EXPECTATION_FAILED)
                .messageCode("Project Creation Failed")
                .build();
    }


    @PostMapping("/delete-directory")
    public MetaDataResponse deleteDirectory(@RequestBody Project project){
        String dir = project.getSourceDir();

       log.info("Deleting dir {} ", dir);


        int result = scriptService.deleteUserProjectDirectory(dir);
        if (result == 1) {
//        Response response = new Response();
            MetaDataResponse response = MetaDataResponse
                    .builder()
                    .httpStatus(HttpStatus.OK)
                    .messageCode("Deleted ")
                    .build();
            return response;
        }
        return MetaDataResponse
                .builder()
                .httpStatus(HttpStatus.EXPECTATION_FAILED)
                .messageCode("Failed")
                .build();


    }






}
