
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class RecursiveDescentPredictiveParser {

     public static void main(String[] args) {
        List<String> tokens = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            scanner.useDelimiter("\\s+"); // Split tokens by whitespace
            while (scanner.hasNext()) {
                tokens.add(scanner.next());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: input.txt");
            return;
        }

        Parser parser = new Parser(tokens);
        try {
            parser.parse();
            System.out.println("Parsing successful.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    }
