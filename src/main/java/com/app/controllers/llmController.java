package com.app.controllers;


import com.app.models.payload.response.MetaDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.app.services.llmService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/authx")
public class llmController {



    @Autowired
    llmService llmService;


//    sign-up so that apis can hit it without
    @PostMapping("/get-response")
    public MetaDataResponse connectWithModel(@RequestBody String prompt){

       return  MetaDataResponse
               .builder()
               .data(llmService.getResponseForPrompt(prompt))
               .messageCode("processed prompt")
               .httpStatus(HttpStatus.OK)
               .build();

    }
}
