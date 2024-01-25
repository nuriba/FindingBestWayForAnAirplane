import java.util.HashMap;
import java.util.LinkedList;
/**
 * The Directions class manages a graph representation of airports and the distances between them.
 * It uses a HashMap to store relationships between airports and their connected nodes.
 */
public class Directions {
    // Stores the graph representation of airports and their connections.
    public static HashMap<Airport,LinkedList<Node>> directionHash = new HashMap<>(); // Store the information for the directed line
    /**
     * Inserts a directed edge between two airports into the graph.
     * @param sourceAirport The starting airport.
     * @param endAirport    The destination airport.
     */
    public void insertToTheMap(Airport sourceAirport,Airport endAirport){
        if(!directionHash.containsKey(sourceAirport))
            directionHash.put(sourceAirport,new LinkedList<>());
        Node node = new Node(endAirport,calculateDistance(sourceAirport, endAirport));
        directionHash.get(sourceAirport).add(node);
    }
    /**
     * Calculates the distance between two airports using their geographical coordinates.
     * It uses the Haversine formula to calculate the great-circle distance between two points on a sphere.
     * @param sourceAirport The starting airport.
     * @param endAirport    The destination airport.
     * @return The distance between the two airports in kilometers.
     */
    public double calculateDistance(Airport sourceAirport,Airport endAirport){
        double lat1 = sourceAirport.latitude; double lon1 = sourceAirport.longitude;
        double lat2 = endAirport.latitude; double lon2 = endAirport.longitude;
        double degree1 = ((lat2-lat1)/2)*Math.PI/180;double degree2 = lat1*Math.PI/180;
        double degree3 = lat2*Math.PI/180;double degree4 = ((lon2-lon1)/2)*Math.PI/180;
        double squareSin1 = Math.pow(Math.sin(degree1),2);double squareSin2 = Math.pow(Math.sin(degree4),2);
        double multiplication = Math.cos(degree2)*Math.cos(degree3)*squareSin2;
        double addition = squareSin1+multiplication;
        double inside = Math.pow(addition,0.5);
        return 2*6371*Math.asin(inside);
    }
}
