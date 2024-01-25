/**
 * The Airport class represents an airport with various attributes including its code name, location, and parking cost.
 */
public class Airport {
    String airportCode; // A unique identifier for the airport
    String airfieldName; // the airfield the airport belongs
    double latitude; // First decimal number to identify the location of the airport
    double longitude; // Second decimal number to identify the location of the airport
    double parkingCost; // The cost of parking at the airport for 6 hours.
    /**
     * Constructs a new Airport instance with specified details.
     * @param airportCode Unique code of the airport.
     * @param airfield    Name of the airfield where the airport is located.
     * @param latitude    Latitude coordinate of the airport.
     * @param longitude   Longitude coordinate of the airport.
     * @param parkingCost Cost of parking at the airport for 6 hours.
     */

    Airport(String airportCode, String airfield, double latitude, double longitude, double parkingCost){
        this.airportCode =airportCode;
        this.airfieldName = airfield;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parkingCost = parkingCost;
    }
    Airport(Airport airport){
        this.airportCode = "PARK";
        this.airfieldName = airport.airfieldName;
        this.latitude = airport.latitude;
        this.longitude = airport.longitude;
        this.parkingCost = airport.parkingCost;
    }

}
