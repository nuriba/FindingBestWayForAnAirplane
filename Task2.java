import java.util.*;
/**
 * This class is designed to find the best path from a source to a destination airport within a given deadline.
 * It takes into account various factors like parking, weather, and distance. It uses a priority queue to
 * efficiently find the least cost path.
 */
public class Task2 {
    HashMap<Airport, LinkedList<Node>> direction; // Graph representing the connections between airports.
    Node bestPath = null;// Best path found from source to destination.
    PriorityQueue<Node> pq;// Priority queue to manage exploring paths in an efficient order.
    HashMap<Airport,HashMap<Long,Double>> costAtTime;// Stores the cost of reaching an airport at a specific time.
    /**
     * Initializes the task and finds the best path from a source airport to a destination airport.
     *
     * @param direction    The graph representing airport connections.
     * @param src          The source airport.
     * @param initialTime  The initial time of departure.
     * @param destination  The destination airport.
     * @param deadline     The deadline to reach the destination.
     */
    public void task2(HashMap<Airport,LinkedList<Node>> direction, Airport src, Long initialTime, Airport destination,Long deadline){
        setDirect(direction);
        Node srcNode = new Node(src,0,0);
        srcNode.time = initialTime;
        bestPath = null;
        costAtTime = new HashMap<>();
        pq = new PriorityQueue<>(direction.size()*5,new Node());
        pq.add(srcNode);
        while (true) {
            if (pq.isEmpty())
                return;
            Node u = pq.remove();
            if (u.airport.airportCode.equals(destination.airportCode)) {
                bestPath = u;
                return;
            }
            findAllPath(u,deadline);
        }

    }
    /**
     * Explores all possible paths from the current node to the destination, considering the deadline.
     *
     * @param src      The current node being explored.
     * @param deadline The deadline for reaching the destination.
     */
    private void findAllPath(Node src, Long deadline){

        if (src.airport.airportCode.equals("PARK")){
            Node parkNode = new Node(direction.get(src.airport).get(0).airport,direction.get(src.airport).get(0).distance);
            parkNode.cost = src.cost;parkNode.time = src.time;
            ArrayList<String> listPark = new ArrayList<>(src.path);
            listPark.add("PARK");
            parkNode.path = listPark;
            pq.add(parkNode);
        }else{

            double cost = -1;
            for (Node node: direction.get(src.airport)){
                Long requiredTime = calculateFlightDuration(node.distance,Main.typeOfAircraft);
                Long newTime;
                ArrayList<String> path = new ArrayList<>(src.path);
                if(node.airport.airportCode.equals("PARK")) {
                    requiredTime = 21600L;
                    newTime = src.time + requiredTime;
                    if(newTime>deadline)
                        continue;
                    cost = src.cost+src.airport.parkingCost;
                }else{
                    newTime = src.time + requiredTime;
                    if (newTime > deadline)
                        continue;
                    cost = src.cost + calculate(Main.airfieldHashMap.get(src.airport.airfieldName).get(src.time), Main.airfieldHashMap.get(node.airport.airfieldName).get(src.time + requiredTime), node.distance);
                }
                if (!path.contains(src.airport.airportCode))
                    path.add(src.airport.airportCode);
                Node newNode = new Node(node.airport,node.distance,cost);
                newNode.path =new ArrayList<>(path); newNode.time = newTime;
                if (costAtTime.getOrDefault(newNode.airport,null)==null)
                    costAtTime.put(newNode.airport,new HashMap<>());
                if(costAtTime.get(newNode.airport).getOrDefault(newTime,null)==null) {
                    costAtTime.get(newNode.airport).put(newTime, cost);
                    pq.add(newNode);
                }
                if(costAtTime.get(newNode.airport).get(newTime)>cost) {
                    costAtTime.get(newNode.airport).put(newTime, cost);
                    pq.add(newNode);
                }
            }
        }

    }
    /**
     * Sets up the direction graph with additional nodes and connections.
     *
     * @param direct The original graph representing airport connections.
     */
    private void setDirect(HashMap<Airport, LinkedList<Node>> direct){
        this.direction = new HashMap<>();
        for (Airport airport: direct.keySet()){
            LinkedList<Node> linkList= new LinkedList<>();
            for (Node node: direct.get(airport)){
                linkList.add(new Node(node.airport,node.distance));
            }
            Airport parkAir = new Airport("PARK",airport.airfieldName,airport.latitude,airport.longitude,airport.parkingCost);
            Node newNode = new Node(parkAir,0);
            linkList.add(newNode);
            LinkedList<Node> linkedList = new LinkedList<>();
            linkedList.add(new Node(airport,0));
            this.direction.put(parkAir,linkedList);
            this.direction.put(airport,linkList);
        }
    }
    /**
     * Calculates the flight duration based on the distance and type of aircraft.
     *
     * @param distance        The distance of the flight.
     * @param typeOfThePlane  The type of aircraft.
     * @return The flight duration in seconds.
     */
    public static Long calculateFlightDuration(double distance, String typeOfThePlane){
        switch (typeOfThePlane) {
            case "Carreidas 160" -> {
                if (distance <= 175)
                    return (long) (6*3600);
                else if (175 < distance && distance <= 350)
                    return (long) (12*3600);
                else
                    return (long) (18*3600);
            }
            case "Orion III" -> {
                if (distance <= 1500)
                    return (long) (6*3600);
                else if (1500 < distance && distance <= 3000)
                    return (long) (12*3600);
                else
                    return (long) (18*3600);
            }
            case "Skyfleet S570" -> {
                if (distance <= 500)
                    return (long) (6*3600);
                else if (500 < distance && distance <= 1000)
                    return (long) (12*3600);
                else
                    return (long) (18*3600);
            }
            case "T-16 Skyhopper" -> {
                if (distance <= 2500)
                    return (long) (6*3600);
                else if (2500 < distance && distance <= 5000)
                    return (long) (12*3600);
                else
                    return (long) (18*3600);
            }
        }
        return 0L;
    }
    /**
     * Calculates the cost of the flight considering weather multipliers and distance.
     *
     * @param weatherMul1 First weather multiplier.
     * @param weatherMul2 Second weather multiplier.
     * @param distance    The distance of the flight.
     * @return The calculated cost of the flight.
     */
    private Double calculate(double weatherMul1,double weatherMul2,double distance){
        return 300*weatherMul1*weatherMul2 + distance;
    }
}
