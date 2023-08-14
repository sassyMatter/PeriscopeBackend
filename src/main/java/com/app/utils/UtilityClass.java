package com.app.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.IOException;


public class UtilityClass {

    public static File OUTPUT_CLASS_DIRECTORY = new File("src/main/java");
    public static String PACKAGE_NAME = "com.app.models.customModels";

    public static void convertJsonToJavaClass(String json, File outputJavaClassDirectory, String packageName, String javaClassName)
            throws IOException {
        JCodeModel jcodeModel = new JCodeModel();


//        ObjectMapper mapper = Json.mapper();
//        // Convert the JSON string to a JsonNode
//        JsonNode jsonNode = mapper.readTree(json);
        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() {
                return true;
            }

            @Override
            public SourceType getSourceType() {
                return SourceType.JSON;
            }
        };

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(jcodeModel, javaClassName, packageName, json);

        jcodeModel.build(outputJavaClassDirectory);
    }
}
