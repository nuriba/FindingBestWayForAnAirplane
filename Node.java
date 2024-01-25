import java.util.ArrayList;
import java.util.Comparator;
/**
 * The Node class represents a node in a graph, specifically for the purpose of finding the shortest path in a network of airports.
 * It includes information about an airport, the distance to it, and the cost associated with reaching it.
 */
public class Node implements Comparator<Node> {
    Airport airport;
    double distance;
    double cost = Double.MAX_VALUE;
    long time = 0;
    ArrayList<String>path = new ArrayList<>(); //Stores to path until this node
    /**
     * Constructs a new Node with specified airport and distance.
     * @param airport  The airport associated with this node.
     * @param distance The distance to this airport.
     */
    Node(Airport airport,double distance){
        this.airport=airport;
        this.distance =distance;
    }
    /**
     * Constructs a new Node with specified airport, distance, and cost.
     * @param airport  The airport associated with this node.
     * @param distance The distance to this airport.
     * @param cost     The cost associated with reaching this airport.
     */
    Node(Airport airport,double distance,double cost){
        this.airport = airport;
        this.distance = distance;
        this.cost = cost;
    }
    Node(){}
    /**
     * Compares two nodes based on their cost. This method used in the priority queue.
     * @param node1 The first node to be compared.
     * @param node2 The second node to be compared.
     * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    @Override public int compare(Node node1, Node node2){
        if (node1.cost < node2.cost)
            return -1;

        if (node1.cost > node2.cost)
            return 1;

        return 0;
    }
}
