import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
/**
 * The Main class for managing airport and weather data, and for processing flight paths.
 */
public class Main {
    public static HashMap<String,HashMap<Long,Double>> airfieldHashMap = new HashMap<>();
    public static HashMap<String,Airport> airportHashMap = new HashMap<>();
    public static String typeOfAircraft;
    /**
     * The main method reads data from various CSV files, processes it, and computes optimal flight paths and costs.
     *
     * @param args Arguments passed to the program (not used).
     * @throws IOException If there is an error in file reading/writing.
     */

    public static void main(String[] args) throws IOException {
        File weatherFile = new File(args[2]);
        Scanner weather = new Scanner(weatherFile);
        weather.nextLine();
        while(weather.hasNextLine()){
            String[] lineList = weather.nextLine().split(",");
            if(!airfieldHashMap.containsKey(lineList[0]))
                airfieldHashMap.put(lineList[0],new HashMap<>());
            airfieldHashMap.get(lineList[0]).put(Long.parseLong(lineList[1]),calculateWeatherMultiplier(Integer.parseInt(lineList[2])));
        }
        String name2 = "";
        File airportFile = new File(args[0]);
        Scanner airports = new Scanner(airportFile);
        airports.nextLine();
        while (airports.hasNextLine()){
            String[] lineList = airports.nextLine().split(",");
            Airport airport = new Airport(lineList[0],lineList[1],Double.parseDouble(lineList[2]),Double.parseDouble(lineList[3]),Double.parseDouble(lineList[4]));
            airportHashMap.put(lineList[0],airport);
        }
        File fileForDirections = new File(args[1]);
        Scanner directionsFile = new Scanner(fileForDirections);
        directionsFile.nextLine();
        Directions directions = new Directions();
        while (directionsFile.hasNextLine()) {
            String[] lineList = directionsFile.nextLine().split(",");
            directions.insertToTheMap(airportHashMap.get(lineList[0]), airportHashMap.get(lineList[1]));
        }
        File fileForTask1 = new File(args[3]);
        Scanner task1 = new Scanner(fileForTask1);
        task1.nextLine();
        FileWriter task1Output = new FileWriter(args[4]);
        Task1 task = new Task1(Directions.directionHash.size());
        while (task1.hasNextLine()) {
            String[] lineList = task1.nextLine().split(" ");
            ArrayList<String> path = new ArrayList<>();
            Double[] cost = new Double[1];
            task.dijkstra(Directions.directionHash,airportHashMap.get(lineList[0]),Long.parseLong(lineList[2]),lineList[1],path,cost);
            //Writes the path to the document
            for (int i= path.size()-1; i>-1;i--){
                task1Output.write(path.get(i)+" ");
                task1Output.flush();
            }
            String cost1 =String.format("%.5f",cost[0]).replaceFirst(",",".");
            task1Output.write(cost1);
            task1Output.flush();
            task1Output.write("\n");
        }
        File fileForTask2 = new File(args[3]);
        Scanner readingMissionsForTask2 = new Scanner(fileForTask2);
        typeOfAircraft = readingMissionsForTask2.nextLine();
        FileWriter task2Output = new FileWriter(args[5]);
        Task2 task2 = new Task2();
        while (readingMissionsForTask2.hasNextLine()){
            String[] lineList = readingMissionsForTask2.nextLine().split(" ");
            task2.task2(Directions.directionHash,airportHashMap.get(lineList[0]),Long.parseLong(lineList[2]),airportHashMap.get(lineList[1]),Long.parseLong(lineList[3]));
            //Writes the path to the document
            if (task2.bestPath != null) {
                for (String name :task2.bestPath.path){
                    task2Output.write(name+" ");
                    task2Output.flush();
                }
                task2Output.write(lineList[1]+" ");
                task2Output.flush();
                String cost1 =String.format("%.5f", task2.bestPath.cost).replaceFirst(",",".");
                task2Output.write(cost1);
                task2Output.flush();
                task2Output.write("\n");
            }else {
                task2Output.write("No possible solution.\n");
                task2Output.flush();
            }
        }
    }
    /**
     * Calculates the weather multiplier based on a given weather code.
     *
     * @param weatherCode The code representing specific weather conditions in a specific Time.
     * @return The calculated weather multiplier.
     */
   public static double calculateWeatherMultiplier(int weatherCode){
       int[] weatherMultipliers = calculateWeatherMultipliers(weatherCode);
       double W_w = (weatherMultipliers[0]*1.05) + (1-weatherMultipliers[0]);
       double W_r =( weatherMultipliers[1]*1.05 )+ (1-weatherMultipliers[1]);
       double W_s = (weatherMultipliers[2]*1.10) + (1-weatherMultipliers[2]);
       double W_h = (weatherMultipliers[3]*1.15) + (1-weatherMultipliers[3]);
       double W_b = (weatherMultipliers[4]*1.20) + (1-weatherMultipliers[4]);
       return W_w * W_r * W_s * W_h * W_b;
   }
    /**
     * Converts a weather code into a binary representation.
     *
     * @param weatherCode The weather code to be converted.
     * @return An array of integers representing weather multipliers.
     */
   public static int[] calculateWeatherMultipliers(int weatherCode){
       int index = 4;
       int weather = weatherCode;
       int[] weatherMultipliers = new int[5];
       while (index>=0){
           weatherMultipliers[index] = weather%2;
           weather /= 2;
           index -= 1;
       }
       return weatherMultipliers;
   }
}