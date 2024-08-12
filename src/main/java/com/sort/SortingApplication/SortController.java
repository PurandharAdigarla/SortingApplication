package com.sort.SortingApplication;

import jakarta.servlet.http.HttpSession;
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
    private static final Path JAVA_SORTED_ASSENT_PATH = Paths.get("/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/javaSortedAssent.csv");
    private static final Path JAVA_SORTED_DESCENT_PATH = Paths.get("/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/javaSortedDescent.csv");

    @PostMapping("/api/generateRandomNumbers")
    public ResponseEntity<String> generateRandomNumbers(@RequestParam("count") int count) {
        if (count <= 0) {
            return ResponseEntity.badRequest().body("Invalid number. Must be a positive integer.");
        }

        try {
            // Ensure the count is passed correctly to the script
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/randomNumber.sh", String.valueOf(count));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the script output and handle it if needed
            process.waitFor();

            // Assuming the script handles output internally and just generates the file
            return ResponseEntity.ok("Random numbers generated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating random numbers.");
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

            Path sortedFilePath = "ascending".equals(sortOrder) ? JAVA_SORTED_ASSENT_PATH : JAVA_SORTED_DESCENT_PATH;
            String sortedFileName = sortedFilePath.getFileName().toString();

            return ResponseEntity.ok().body(Map.of(
                    "timeTaken", timeTaken,
                    "randomNumbersUrl", "/downloads/randomNumbers.csv",
                    "sortedNumbersUrl", "/downloads/" + sortedFileName
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

    @GetMapping("/downloads/javaSortedAssent.csv")
    public ResponseEntity<Resource> downloadSortedAssentNumbers() {
        return downloadFile(JAVA_SORTED_ASSENT_PATH);
    }

    @GetMapping("/downloads/javaSortedDescent.csv")
    public ResponseEntity<Resource> downloadSortedDescentNumbers() {
        return downloadFile(JAVA_SORTED_DESCENT_PATH);
    }

    private ResponseEntity<Resource> downloadFile(Path filePath) {
        try {
            Resource file = new UrlResource(filePath.toUri());
            if (file.exists() && file.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                        .body(file);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}