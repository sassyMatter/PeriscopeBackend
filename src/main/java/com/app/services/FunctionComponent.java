package com.app.services;

import com.app.services.interfaces.CodeComponent;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Builder
@NoArgsConstructor(force = true)
public class FunctionComponent implements CodeComponent {
    private final Map<String, String> parameters;
    private final String returnType;
    private final String functionBody;
    private final String functionName;

    // function type : computation, listener, producer
    private final String functionType;


    // there will be complex method too, where dependencies and import would be required,
    // we will look into it later

    public FunctionComponent(Map<String, String> parameters, String returnType, String functionBody, String functionName, String functionType) {
        this.parameters = parameters;
        this.returnType = returnType;
        this.functionBody = functionBody;
        this.functionName = functionName;
        this.functionType = functionType;
    }

    @Override
    public String generateCode() {
        // Generate code for the function
        StringBuilder codeBuilder = new StringBuilder();

        // Append method signature
        // modifier can be changed later
        codeBuilder.append("   public ");
        codeBuilder.append(returnType).append(" " + functionName).append("(");
        boolean isFirstParameter = true;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (!isFirstParameter) {
                codeBuilder.append(", ");
            }
            codeBuilder.append(entry.getKey()).append(" ").append(entry.getValue());
            isFirstParameter = false;
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
}

