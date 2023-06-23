package com.app.controllers;



import com.app.models.Response;
import com.app.producers.TestProducer;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@CrossOrigin(origins="*" , allowedHeaders = "*")
@RequestMapping("/hello" )
@Slf4j
public class Sample {

    @Autowired
    TestProducer testProducer;

    @Autowired
    Gson gson;

    @GetMapping
    public Response Sample(){
        log.info("Hit");
        Response response = new Response();
        response.setResponse("Hello, Connected to server New");

        testProducer.sendMessage("testTopic", gson.toJson(response));
        return response;
    }







}
