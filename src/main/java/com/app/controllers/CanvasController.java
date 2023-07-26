package com.app.controllers;



import com.app.models.Response;
import com.app.models.canvas.CanvasData;
import com.app.models.canvas.CanvasObject;
import com.app.models.canvasSchema.TreeNode;
import com.app.services.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/canvas")
@CrossOrigin(origins="*")
@Slf4j
public class CanvasController {

    @Autowired
    CanvasService canvasService;

    @Autowired
    TreeBuilderService treeBuilderService;

    @Autowired
    CodeWriterService codeWriterService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    KafkaAdmin kafkaAdmin;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @PostMapping("/post-canvas-data")
    public Response PostCanvasData(@RequestBody CanvasData data){
        log.info("Saving canvas data {} ", data);
        canvasService.saveCanvasData(data);
        Response response = new Response();
        String res = "Received data :: " + data;
        response.setResponse(res);
        return response;
    }

    @GetMapping("/get-canvas")
    public ResponseEntity<?> getAllCanvasData(){
        log.info("Getting canvas Data...");
        List<CanvasData> data =  canvasService.getAllCanvasData();
        return ResponseEntity.ok(data);

    }

    @PostMapping("/run-simulation")
    public Response RunSimulationJob(@RequestBody CanvasData data){
        log.info("Saving canvas data");

        log.info("Starting simulation job");
        // will start the whole process of creating Tree and and its implementation, meanwhile
        // status would be set to IN_PROGRESS

        // 1. Get Data, for now from DB
        // 2. Create tree
        // 3. Print Tree with processing each node
        // 4. Start writing components: and codeGenerator for same




        return null;

    }

    @PostMapping("/test")
    public Response testController(){

        List<CanvasData> dataList = canvasService.getAllCanvasData();

        for(int i = 0; i < dataList.size(); i++){

            CanvasData data = dataList.get(i);
            TreeNode root = treeBuilderService.buildTree(data);

            log.info("root :: {}", root);

            treeBuilderService.traverseTree(root);

        }


        // testing component's with code-writer
        log.info("Testing components 1.RestInterface \n 2.Function \n 3.Database");

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("String", "var");
        requestBody.put("Response", "response");

        RestComponent restComponent = RestComponent
                                     .builder()
                .apiType("inbound")
                .url("/ge")
                .requestBody(requestBody)
                .headers(new HashMap<>())
                .httpMethod("PostMapping")
                .methodName("sumOfNumbers")
                .requestUrl("/test")
                .type("rest")
                .build();

        String generatedCode = restComponent.generateCode();
        log.info("Generated Code:: {} ", generatedCode);

        codeWriterService.writeToFile(generatedCode, "rest");

        FunctionComponent functionComponent = FunctionComponent
                .builder()
                .functionBody("   int x = 7; \n    int y = 8; \n   System.out.println(x+y); \n  return x+y;")
                .parameters(requestBody)
                .functionName("sumOfNumbers")
                .returnType("int")
                .build();

        String functionCode = functionComponent.generateCode();
        log.info("Generated Code for function:: {} ", functionCode);
        codeWriterService.writeToFile(functionCode, "function");

        List<String> tableDefinitions = new ArrayList<>();
        tableDefinitions.add("CREATE TABLE testTable1 (\n" +
                "  id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "  column1 VARCHAR(255),\n" +
                "  column2 VARCHAR(255)\n" +
                ");");

        tableDefinitions.add("CREATE TABLE testTable2 (\n" +
                "  id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "  column1 VARCHAR(255),\n" +
                "  column2 VARCHAR(255)\n" +
                ");");

        DatabaseComponent databaseComponent = DatabaseComponent
                .builder()
                .jdbcTemplate(jdbcTemplate)
                .tableDefinitions(tableDefinitions)
                .build();

        databaseComponent.generateCode();

        log.info("Creating queue component...");
        QueueComponent queueComponent = QueueComponent
                .builder()
                .topic("TestQueue")
                .kafkaAdmin(kafkaAdmin)
                .build();
        log.info("Queue component {} ", queueComponent);
        queueComponent.configureQueue();



        Response res = Response.builder().response(generatedCode + "\n\n\n\n" + functionCode).build();
        return res;
    }


    @PostMapping("/simulation-test")
    public Response simulationController(){

        List<CanvasData> dataList = canvasService.getAllCanvasData();

        for(int i = 0; i < dataList.size(); i++){

            CanvasData data = dataList.get(i);
            TreeNode root = treeBuilderService.buildTree(data);

            log.info("root :: {}", root);

//            treeBuilderService.traverseTree(root);
            treeBuilderService.processGraph(root);

        }

//        CanvasData data = dataList.get(0);
//
//        List<CanvasObject> objects = data.getObjects();
//        for(int i = 0; i < objects.size(); i++){
//            CanvasObject object = objects.get(i);
//            log.info("type is :: {}, \n object :: {}",  object.getType(), object);
//            if(Objects.equals(object.type, "rest")){
//                // generate code for rest
//                RestComponent restComponent = RestComponent
//                        .builder()
//                        .apiType(object.getApiType())
//                        .url(object.getUrl())
////                        .requestBody()
//                        .headers(new HashMap<>())
//                        .httpMethod(object.getHttpMethod())
//                        .methodName(object.getMethodName())
//                        .requestUrl(object.requestUrl)
//                        .requestBody(object.requestBody)
//                        .type(object.getType())
//                        .build();
//
//                String generatedCode = restComponent.generateCode();
//
//                codeWriterService.writeToFile(generatedCode, "rest");
//                log.info("Generated Code for rest {} ", generatedCode);
//
//            }
//            if(Objects.equals(object.type, "func")){
//                // generate code for func
//                FunctionComponent functionComponent = FunctionComponent
//                        .builder()
//                        .functionBody(object.functionBody)
//                        .parameters(object.parameters)
//                        .functionName(object.functionName)
//                        .functionType(object.functionType)
//                        .returnType(object.returnType)
//                        .build();
//
//                String functionCode = functionComponent.generateCode();
//                log.info("Generated Code for function:: {} ", functionCode);
//                codeWriterService.writeToFile(functionCode, "function");
//                log.info("Generated Code for function {} ", functionCode);
//            }
//            if(Objects.equals(object.type, "database")){
//                // generate code for database
//                DatabaseComponent databaseComponent = DatabaseComponent
//                        .builder()
//                        .jdbcTemplate(jdbcTemplate)
//                        .tableDefinitions(object.tableDefinitions)
//                        .build();
//
//                databaseComponent.generateCode();
//                log.info("Configured Database");
//            }
//            if(Objects.equals(object.type, "queue")){
//                // generate code for queue
//                log.info("Creating queue component...");
//                QueueComponent queueComponent = QueueComponent
//                        .builder()
//                        .topic(object.topic)
//                        .kafkaAdmin(kafkaAdmin)
//                        .build();
//
//                queueComponent.configureQueue();
//                log.info("Configured queue component {} ", queueComponent);
//            }
//            log.info("Simulation would start in next run, it is configured");
//        }

        Response res = Response.builder().response("Simulation Success").build();
        return res;
    }




}
