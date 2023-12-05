import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class performanceParser {

    String filePath = "test.txt";
    ArrayList<Integer> servletTimes = new ArrayList<>();
    ArrayList<Integer> jdbcTimes = new ArrayList<>();

    private void parseFile(){
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");

                if (parts.length == 2) {
                    servletTimes.add(Integer.parseInt(parts[0]));
                    jdbcTimes.add(Integer.parseInt(parts[1]));
                } else {
                    System.err.println("Invalid line: " + line);
                }
            }

            scanner.close();

            System.out.println("First Integers: " + servletTimes);
            System.out.println("Second Integers: " + jdbcTimes);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    /*
    Returns array of length 2:
    first index stores the average servlet time.
    second index stores the average JDBC time.
     */
    private double[] findAverages(){
        double[] averages = new double[2];
        averages[0] = (double) calcSum(servletTimes) / servletTimes.size();
        averages[1] = (double) calcSum(jdbcTimes) / jdbcTimes.size();
        return averages;
    }

    private int calcSum(ArrayList<Integer> list){
        int sum = 0;
        for (int element : list) { sum += element; }
        return sum;
    }


    public static void main(String[] args) {
        performanceParser parser = new performanceParser();
        parser.parseFile();
        double[] averages = parser.findAverages();
        System.out.println(averages[0] + " " + averages[1]);
    }
}