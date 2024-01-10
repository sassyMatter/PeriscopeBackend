package com.app.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

@Service
@Slf4j
public class llmService {

    @Value("${com.userSpace.llmDirectory}")
    public  String llmDirectory;


    public String getResponseForPrompt(String prompt){
        try {
            // Start the Alpaca model as a subprocess
            ProcessBuilder processBuilder = new ProcessBuilder("./chat");
            processBuilder.directory(new File(llmDirectory));
            processBuilder.redirectErrorStream(true);
            Process alpacaProcess = processBuilder.start();


            log.info("Built alpacaProcess");

//             Send an initial newline to the subprocess
            try (OutputStream stdin = alpacaProcess.getOutputStream()) {
                log.info("sending new line to alpaca process");
                stdin.write("\n".getBytes(StandardCharsets.UTF_8));
                stdin.flush();
            }

//             Test the function with different prompts
            String[] prompts = new String[]{prompt};
            for (String pro : prompts) {
                log.info("processing prompt {} ", pro);
                String response = alpacaPredict(pro, alpacaProcess);
                log.info("Prompt: " + prompt + " - Response: " + response);
                return response;
            }

            // Wait for the process to complete
            int exitCode = alpacaProcess.waitFor();
            log.info("Alpaca process exited with code: " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private  String alpacaPredict(String prompt, Process alpacaProcess) throws IOException {
        log.info("Trying to predict output");
        try (OutputStream stdin = alpacaProcess.getOutputStream()) {
            log.info("Sending prompt to Alpaca: {}", prompt);
            stdin.write((prompt + "\n").getBytes(StandardCharsets.UTF_8));
            stdin.flush();
            Thread.sleep(1000);
        } // Close the stdin stream here
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader stdout = new BufferedReader(new InputStreamReader(alpacaProcess.getInputStream()))) {
            return stdout.readLine();
        }
    }


}
