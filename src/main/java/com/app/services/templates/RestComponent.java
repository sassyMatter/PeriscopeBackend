package com.app.services.templates;

import com.app.services.interfaces.CodeComponent;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Builder
@NoArgsConstructor(force = true)
public class RestComponent implements CodeComponent {
    private final String url;
    private final String type;
    private final Map<String, String> headers;
    private final Map<String, String> requestBody;
    private final Map<String, String> parameters;
    private final String requestUrl;
    private final String apiType;
    private final String httpMethod;


    // should have a list of method names and their return value
    private final String methodName;

    public RestComponent(String url, String type, Map<String, String> headers,
                         Map<String, String> requestBody, Map<String, String> parameters, String requestUrl,
                         String apiType, String httpMethod, String methodName) {
        this.url = url;
        this.type = type;
        this.headers = headers;
        this.requestBody = requestBody;
        this.parameters = parameters;
        this.requestUrl = requestUrl;
        this.apiType = apiType;
        this.httpMethod = httpMethod;
        this.methodName = methodName;

    }

    @Override
    public String generateCode() {
        // Generate code to create a REST API in a Spring Boot application with multiple properties
        StringBuilder codeBuilder = new StringBuilder();

        codeBuilder.append("    @Autowired").append("\n");
        codeBuilder.append("    private CustomCodeService myService;").append("\n");
        codeBuilder.append("\n");
        codeBuilder.append("    @").append(httpMethod).append("(\"").append(requestUrl).append("\")").append("\n");
        codeBuilder.append("    public ResponseEntity<?> ").append("StaticNameForEndpoint").append("(");

        // Generate code for headers
        if (headers != null && !headers.isEmpty()) {
            codeBuilder.append("@RequestHeader Map<String, String> headers, ");
        }

        // Generate code for request body
        if (requestBody != null && !requestBody.isEmpty()) {
            for (Map.Entry<String, String> entry : requestBody.entrySet()) {
                String variableName = entry.getKey();
                String variableType = entry.getValue();
                codeBuilder.append(variableType).append(" ").append(variableName).append(", ");
            }
        }

        // Remove trailing comma and space if request body is present
        if (requestBody != null && !requestBody.isEmpty()) {
            codeBuilder.setLength(codeBuilder.length() - 2);
        }

        codeBuilder.append(") {").append("\n");
        codeBuilder.append("        // Process the request and perform desired operations").append("\n");
        codeBuilder.append("        // ...").append("\n");
        codeBuilder.append("        myService.").append(methodName).append("(");

        // Generate code for request parameters
        if (headers != null && !headers.isEmpty()) {
            codeBuilder.append("headers, ");
        }

        if (requestBody != null && !requestBody.isEmpty()) {
            codeBuilder.append(requestBody.keySet().toString().replaceAll("[\\[\\]]", ""));
        }

        // adding support for sending data back
        codeBuilder.append(");").append("\n");
        codeBuilder.append("        return ResponseEntity.ok().build();").append("\n");
        codeBuilder.append("    }").append("\n");

        return codeBuilder.toString();
    }
}
