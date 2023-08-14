package com.app.services;

import com.app.services.interfaces.CodeComponent;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Builder
@NoArgsConstructor(force = true)
@Slf4j
public class FunctionComponent implements CodeComponent {
    private final Map<String, String> parameters;
    private final String returnType;
    private final String functionBody;
    private final String functionName;

    // topic to which we need to push and consume from
    private final String topic;


    // to be used for converting to and from json
    private String deserializationClass;

    // function type : computation, listener, producer
    private final String functionType;


    // there will be complex method too, where dependencies and import would be required,
    // we will look into it later

    public FunctionComponent(Map<String, String> parameters, String returnType, String functionBody, String functionName, String topic, String dataClassforQueue, String functionType) {
        this.parameters = parameters;
        this.returnType = returnType;
        this.functionBody = functionBody;
        this.functionName = functionName;
        this.topic = topic;
        this.deserializationClass = dataClassforQueue;
        this.functionType = functionType;
    }

    @Override
    public String generateCode() {
        log.info("generating function code {} ", functionType);
        switch(functionType) {


            case "Vanilla":
                log.info("generating code for functionType: {} ", functionType);
                return generateVanillaFunction();
            case "Consumer":
                return generateConsumer();
            case "producer":
                // later can be changed, for now the scoped function inside
                // this can use kafkaProducer variable
                // to send message to a topic
                return generateVanillaFunction();
            default:
                log.info("Invalid Function Type");
        }
        return generateVanillaFunction();
    }

    private String generateVanillaFunction() {
        // Generate code for the function
        StringBuilder codeBuilder = new StringBuilder();

        // Append method signature
        // modifier can be changed later
        codeBuilder.append("   public ");
        codeBuilder.append(returnType).append(" " + functionName).append("(");
        boolean isFirstParameter = true;
        log.info("parameters:: {} ", parameters);
        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (!isFirstParameter) {
                    codeBuilder.append(", ");
                }
                codeBuilder.append(entry.getValue()).append(" ").append(entry.getKey());
                isFirstParameter = false;
            }
        }
        codeBuilder.append(") {").append("\n");

        // Append function body
        codeBuilder.append("     " + functionBody).append("\n");

        // Append return statement if the return type is not void
//        if (!returnType.equals("void")) {
//            codeBuilder.append("    return <returnValue>;").append("\n");
//        }

        codeBuilder.append("   }").append("\n");

        return codeBuilder.toString();
    }


    private String generateConsumer() {
//        this.deserializationClass = "CustomType";
        StringBuilder codeBuilder = new StringBuilder();

        // Append annotations
        codeBuilder.append("@KafkaListener(topics = \"").append(topic + "\"").append(")").append("\n");

        // Append method signature
        codeBuilder.append("public void process").append(toCamelCase(topic)).append("(String ")
                .append(toCamelCase(topic)).append("JSON)").append(" {").append("\n");

        // Append logging statement
        codeBuilder.append("    log.info(\"received content = '{}'\", ").append(toCamelCase(topic)).append("JSON);")
                .append("\n");

        // Append try-catch block for deserialization
        codeBuilder.append("    try {").append("\n");
        codeBuilder.append("        ObjectMapper mapper = new ObjectMapper();").append("\n");
        codeBuilder.append("        ").append(deserializationClass).append(" ").append(toCamelCase(topic))
                .append(" = mapper.readValue(").append(toCamelCase(topic)).append("JSON, ")
                .append(deserializationClass).append(".class);").append("\n");
        codeBuilder.append("        // Add your custom processing logic here").append("\n");
        codeBuilder.append("        log.info(\"Success parsing received object ")
                .append(" '{}' with topic '{}'\", ").append(toCamelCase(topic))
                .append(");").append("\n");
        codeBuilder.append("    } catch (Exception e) {").append("\n");
        codeBuilder.append("        log.error(\"An error occurred! '{}'\", e.getMessage());").append("\n");
        codeBuilder.append("    }").append("\n");

        codeBuilder.append("}").append("\n");

        return codeBuilder.toString();
    }

    private String toCamelCase(String input) {
        String[] words = input.split("[^\\w]+");
        StringBuilder camelCaseBuilder = new StringBuilder(words[0].toLowerCase());
        for (int i = 1; i < words.length; i++) {
            camelCaseBuilder.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1).toLowerCase());
        }

        return camelCaseBuilder.toString();
    }


}

