import java.util.*;
/**
 * The class implements Dijkstra's algorithm to find the shortest path between airports.
 */
public class Task1 {
    HashMap<String,Double> cost;// Stores the cost of reaching each airport
    public Set<Airport> settled; //Set of airports for which the minimum cost is calculated
    private PriorityQueue<Node> pq;// Priority queue for the Dijkstra algorithm
    private final int V;// Number of airports in the graph
    HashMap<Airport,LinkedList<Node>> direction; // Graph
    HashMap<String,String> previous; // Predecessor array
    /**
     * Constructor for the class.
     * @param V The number of airports in the graph.
     */
    public Task1(int V) {
        this.V = V;

    }
    /**
     * Implements Dijkstra's algorithm to find the shortest path from a source airport to a destination.
     *
     * @param direction   The graph
     * @param src         The source airport.
     * @param time        The time at which the journey takes place.
     * @param destination The destination airport.
     * @param path        The list to store the path of the journey.I used list to handle the path in the main class.
     * @param finalCost   The array to store the final cost of the journey. I used list to handle the cost in the main class.
     */
    public void dijkstra(HashMap<Airport,LinkedList<Node>> direction,Airport src,Long time,String destination,ArrayList<String> path,Double[] finalCost) {
        cost = new HashMap<>();
        previous = new HashMap<>();
        settled = new HashSet<>();
        pq = new PriorityQueue<>(V, new Node());
        this.direction = new HashMap<>(direction);
        setCost(direction);
        pq.add(new Node(src, 0,0));
        cost.put(src.airportCode,(double) 0);
        while (settled.size() != V) {
            if (pq.isEmpty())
                return;
            Airport u = pq.remove().airport;

            if (settled.contains(u))
                continue;

            settled.add(u);

            e_Neighbours(u,time);
        }
        finalCost[0] = cost.get(destination);
        path.add(destination);
        while (!src.airportCode.equals(destination)){
            path.add(previous.get(destination));
            destination = previous.get(destination);
        }

    }
    /**
     * Explores the neighbours of a given airport and updates the cost and path.
     *
     * @param u    The airport being explored.
     * @param time The time at which the journey takes place.
     */
    private void e_Neighbours(Airport u,Long time) {
        double edgeCost =-1;
        double newCost =-1;

        for (Node v : direction.get(u)) {
            if (!settled.contains(v.airport)) {
                edgeCost = calculate(Main.airfieldHashMap.get(u.airfieldName).get(time),Main.airfieldHashMap.get(v.airport.airfieldName).get(time), v.distance);
                newCost = cost.get(u.airportCode) + edgeCost;

                if (newCost < cost.get(v.airport.airportCode)) {
                    cost.put(v.airport.airportCode,newCost);
                    previous.put(v.airport.airportCode,u.airportCode); // Update predecessor
                }
                pq.add(new Node(v.airport,v.distance,cost.get(v.airport.airportCode)));
            }
        }
    }
    /**
     * Initializes the cost map by using the direction map.
     *
     * @param direct The graph representation.
     */
    private void setCost(HashMap<Airport,LinkedList<Node>> direct){
        for (Airport airport:direct.keySet()){
            cost.put(airport.airportCode,Double.MAX_VALUE);
        }
    }
    /**
     * Calculates the cost between two airports based on weather multipliers and distance.
     * @param weatherMul1 The weather multiplier for the source airport.
     * @param weatherMul2 The weather multiplier for the destination airport.
     * @param distance    The distance between the two airports.
     * @return The cost.
     */
    private Double calculate(double weatherMul1,double weatherMul2,double distance){
        return 300*weatherMul1*weatherMul2 + distance;
    }
}