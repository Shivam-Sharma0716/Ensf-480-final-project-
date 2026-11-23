package flightres;

import java.time.Duration;
import java.time.LocalDateTime;

public class Flight {
    private int flightId;
    private String flightNumber, origin, destination, airline;
    private LocalDateTime departureTime, arrivalTime;
    private double price;
    private int availableSeats;

    public Flight(int flightId, String flightNumber, String origin, String destination,
                  String airline, LocalDateTime departureTime, LocalDateTime arrivalTime,
                  double price, int availableSeats) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.airline = airline;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    public boolean hasAvailableSeats() { return availableSeats > 0; }
    public Duration getDuration() { return Duration.between(departureTime, arrivalTime); }
    public boolean matchesCriteria(SearchCriteria c) { return c.matches(this); }

    public int getFlightId() { return flightId; }
    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getAirline() { return airline; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public double getPrice() { return price; }
    public int getAvailableSeats() { return availableSeats; }
    public void decrementSeat() { if (availableSeats > 0) availableSeats--; }
}