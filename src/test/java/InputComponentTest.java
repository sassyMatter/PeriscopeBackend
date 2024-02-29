//import com.app.services.templates.InputComponent;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@Slf4j
//@RunWith(SpringRunner.class)
//public class InputComponentTest {
//
//    @Autowired
//    private InputComponent inputComponent;
//
//    @Value("${test.outputClassDirectory}") // Provide the test output class directory
//    private String testOutputClassDirectory;
//
//    @Test
//    public void testGenerateCode() throws IOException {
//        // Define a test custom type
//        Map<String, String> testCustomTypes = Map.of(
//                "TestType", "{\"property\":\"value\"}"
//        );
//
//        // Call the generateCode method
//        inputComponent.setCustomTypes(testCustomTypes);
//        inputComponent.setOutputClassDirectory(testOutputClassDirectory);
//        inputComponent.generateCode();
//
//        // Check if the Java file is created in the specified directory
//        String javaFilePath = testOutputClassDirectory + File.separator + "TestType.java";
//        assertTrue(new File(javaFilePath).exists(), "Java file should be created");
//
//        // Add additional checks if needed, e.g., check the content of the generated Java file
//
//        // Clean up the generated file after the test
//        Files.deleteIfExists(Paths.get(javaFilePath));
//    }
//}
