package com.app.services.core;

import com.app.models.canvas.CanvasData;
import com.app.models.canvas.CanvasObject;
import com.app.models.canvas.Connection;
import com.app.models.canvasSchema.TreeNode;
import com.app.models.enums.ConfigurationNodes;
import com.app.models.enums.ExecutionNodes;
import com.app.services.templates.*;
import com.app.utils.SysConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TreeBuilderService implements com.app.services.interfaces.TreeBuilderService {

    @Autowired
    CodeWriterService codeWriterService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    KafkaAdmin kafkaAdmin;

    @Override
    public TreeNode buildTree(CanvasData objects){

        List<TreeNode> treeNodes = convertObjects(objects);
        log.info("Tree Nodes :: {}", treeNodes);
//      populating child nodes for each node and setting up the root for the same
//      Map to hold canvas objects
        TreeNode root = null;
        Map<String, TreeNode> treeNodeMap = new HashMap<>();


        for(int i = 0; i < treeNodes.size(); i++){
            TreeNode node = treeNodes.get(i);
            treeNodeMap.put(node.getData().getId(), node);
            if(Objects.equals(node.getData().getType(), "input")){
                root = node;
            }
        }

        log.info("Tree node map :: {} ", treeNodeMap);
        for(int i = 0; i < treeNodes.size(); i++){
            TreeNode node = treeNodes.get(i);

            log.info("creating for :: {}" , node);
            List<Connection> connections = node.data.getConnections();
            if(connections != null) {
                connections.forEach(connection -> {
                    node.addChild(treeNodeMap.get(connection.getId()));
                });
            }

        }
        return root;
    }


    public  List<TreeNode> convertObjects(CanvasData oldObjects) {
        List<TreeNode> newObjects = oldObjects.getObjects().stream()
                .map(this::createNewObject)
                .collect(Collectors.toList());
        return newObjects;
    }

    public TreeNode createNewObject(CanvasObject canvasObject){
        return new TreeNode(canvasObject);
    }




    public void traverseTree(TreeNode root) {
        if (root == null) {
            return;
        }

        // Process the current node
        processNode(root);

        // Traverse the connected nodes recursively
        for (TreeNode child : root.getConnections()) {
            traverseTree(child);
        }
    }

    public void processNode(TreeNode node) {
        // Perform processing on the node
        System.out.println("Processing node: " + node.getData().getType());
        if("rest".equals(node.getData().getType())){

        }
    }

    /**
     *  version 0.1
     * @param rootNode goes to rootNode executing configuration nodes immediately and execution node eventually
     * error reporting in execution of any node can be reported from here, this kind of feedback would be essential in future
     *
     *  version 0.2
     *  Now there is effectively no difference between configuration and execution node, but we are keeping this structure
     *  for future requirements
     *
     */

    public void processGraph(TreeNode rootNode) {
        log.info("Starting simulation...");
        Stack<TreeNode> globalStack = new Stack<>();
        Stack<TreeNode> configStack = new Stack<>();
        globalStack.push(rootNode);
        Set<String> processedNodes = new HashSet<>();

        while (!globalStack.isEmpty()) {
            TreeNode current = globalStack.pop();

            // Separate configuration nodes and execution nodes
            if (getNodeExecutionType(current.data.getType()).equals(SysConstants.EXECUTION) && !processedNodes.contains(current.data.id)) {

                configStack.push(current);
                processedNodes.add(current.data.id);

            }
            if(getNodeExecutionType(current.data.getType()).equals(SysConstants.CONFIGURATION) && !processedNodes.contains(current.data.id)) {
                processConfigurationNode(current);
                processedNodes.add(current.data.id);
            }


            for (int i = 0; i < current.connections.size(); i++) {
                TreeNode child = current.connections.get(i);

                if(!processedNodes.contains(child.data.id)) {
                    globalStack.push(child);
                    processedNodes.add(current.data.id);
                }

            }
        }

        // Process configuration nodes
        while (!configStack.isEmpty()) {
            TreeNode currentConfigNode = configStack.pop();
            processExecutionNode(currentConfigNode);
        }
    }


    private void processExecutionNode(TreeNode node) {
        // Process execution node
        System.out.println("Processing execution node: " + node.data);
        CanvasObject object = node.data;
        log.info("type is :: {}, \n object :: {}",  object.getType(), object);
        if(Objects.equals(object.type, "rest")){
            // generate code for rest
            RestComponent restComponent = RestComponent
                    .builder()
                    .apiType(object.getApiType())
                    .url(object.getUrl())
                    .headers(new HashMap<>())
                    .httpMethod(object.getHttpMethod())
                    .methodName(object.getMethodName())
                    .requestUrl(object.requestUrl)
                    .requestBody(object.requestBody)
                    .type(object.getType())
                    .build();

            String generatedCode = restComponent.generateCode();

            codeWriterService.writeToFile(generatedCode, "rest");
            log.info("Generated Code for rest {} ", generatedCode);

        }
        if(Objects.equals(object.type, "func")){
            // generate code for func
            FunctionComponent functionComponent = FunctionComponent
                    .builder()
                    .functionBody(object.functionBody)
                    .parameters(object.parameters)
                    .functionName(object.functionName)
                    .functionType(object.functionType)
                    .returnType(object.returnType)
                    .topic(object.topic)
                    .deserializationClass(object.deserializationClass)
                    .build();

            String functionCode = functionComponent.generateCode();
            log.info("Generated Code for function:: {} ", functionCode);
            codeWriterService.writeToFile(functionCode, "function");

        }
    }

    private void processConfigurationNode(TreeNode node) {
        // Process configuration node
        System.out.println("Processing configuration node: " + node.data);
        CanvasObject object = node.data;
        if(Objects.equals(object.type, "database")){
            // generate code for database
            DatabaseComponent databaseComponent = DatabaseComponent
                    .builder()
                    .jdbcTemplate(jdbcTemplate)
                    .tableDefinitions(object.tableDefinitions)
                    .build();

            databaseComponent.generateCode();
            log.info("Configured Database");
        }
        if(Objects.equals(object.type, "queue")){
            // generate code for queue
            log.info("Creating queue component...");
            QueueComponent queueComponent = QueueComponent
                    .builder()
                    .topic(object.topic)
                    .kafkaAdmin(kafkaAdmin)
                    .build();

            queueComponent.configureQueue();
            log.info("Configured queue component {} ", queueComponent);
        }
        if(Objects.equals(object.type, "input")){
            log.info("Creating input component...");
            InputComponent inputComponent = InputComponent
                    .builder()
                    .customTypes(object.customTypes)
                    .build();
            inputComponent.generateCode();
            log.info("Configured input component {} ", inputComponent);
        }
    }

    private String getNodeExecutionType(String type) {

        if(EnumUtils.isValidEnum(ExecutionNodes.class,  ExecutionNodes.getByValue(type).name())){
//            log.info("Execution Node :: {} ", type , " value::  " + ExecutionNodes.getByValue(type).name());
            log.info("Execution Node real value :: {} ",  ExecutionNodes.getByValue(type).name());
            if(!ExecutionNodes.getByValue(type).name().equals("INVALID")) {
                return SysConstants.EXECUTION;
            }

        }

        if(EnumUtils.isValidEnum(ConfigurationNodes.class, ConfigurationNodes.getByValue(type).name())){
            log.info("Configuration Node :: {}", type);
            if(!ConfigurationNodes.getByValue(type).name().equals("INVALID"))
                 return SysConstants.CONFIGURATION;
        }
       return SysConstants.INVALID;
    }










}
