import java.io.*;
import java.util.Arrays;

public class JavaBubbleSortingDescent extends SortTech {

    public static void main(String[] args) {
        String inputFilePath = "/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/randomNumbers.csv";
        String outputFilePathDescent = "/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/javaSortedDescent.csv";
        int cols = 100;

        JavaBubbleSortingDescent sorter = new JavaBubbleSortingDescent();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            String[] tokens = sb.toString().split(",\\s*");
            long[] numbers = Arrays.stream(tokens)
                    .mapToLong(Long::parseLong)
                    .toArray();

            long startTime = System.nanoTime();
            sorter.bubbleSortDescending(numbers);
            long endTime = System.nanoTime();

            double elapsedTime = (endTime - startTime) / 1e6; // Time in milliseconds

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePathDescent));
            for (int i = 0; i < numbers.length; i++) {
                writer.write(Long.toString(numbers[i]));
                if ((i + 1) % cols == 0) {
                    writer.newLine();
                } else {
                    writer.write(", ");
                }
            }
            writer.close();

            System.out.println("Sorted numbers saved to " + outputFilePathDescent);
            System.out.printf("Sorting time: %.2f milliseconds%n", elapsedTime);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}