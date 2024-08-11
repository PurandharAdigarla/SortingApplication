package com.sort.SortingApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
public class SortController {

    private static final Path RANDOM_NUMBERS_PATH = Paths.get("/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/randomNumbers.csv");
    private static final Path SORTED_NUMBERS_PATH = Paths.get("/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/javaSortedAssent.csv");

    @PostMapping("/api/generateRandomNumbers")
    public ResponseEntity<String> generateRandomNumbers() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/randomNumber.sh");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            process.waitFor();
            return ResponseEntity.ok(output.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating random numbers: " + e.getMessage());
        }
    }

    @PostMapping("/api/sortNumbers")
    public ResponseEntity<?> sortNumbers(@RequestParam("order") String order) {
        try {
            String sortOrder = "1".equals(order) ? "ascending" : "2".equals(order) ? "descending" : "ascending";
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/script.sh", sortOrder);
            processBuilder.redirectErrorStream(true);

            long startTime = System.currentTimeMillis();
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
            }

            process.waitFor();
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;

            // Log output and errors
            System.out.println("Script output: " + output.toString());
            System.err.println("Script errors: " + errorOutput.toString());

            if (process.exitValue() != 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Script execution failed: " + errorOutput.toString());
            }

            return ResponseEntity.ok().body(Map.of(
                    "timeTaken", timeTaken,
                    "randomNumbersUrl", "/downloads/randomNumbers.csv",
                    "sortedNumbersUrl", "/downloads/sortedNumbers.csv"
            ));
        } catch (Exception e) {
            System.err.println("Error sorting numbers: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sorting numbers: " + e.getMessage());
        }
    }

    @GetMapping("/downloads/randomNumbers.csv")
    public ResponseEntity<Resource> downloadRandomNumbers() {
        return downloadFile(RANDOM_NUMBERS_PATH);
    }

    @GetMapping("/downloads/sortedNumbers.csv")
    public ResponseEntity<Resource> downloadSortedNumbers() {
        return downloadFile(SORTED_NUMBERS_PATH);
    }

    private ResponseEntity<Resource> downloadFile(Path filePath) {
        try {
            Resource file = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}