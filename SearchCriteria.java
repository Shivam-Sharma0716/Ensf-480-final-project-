package flightres;

import java.time.LocalDate;

public class SearchCriteria {
    private String origin, destination, airline;
    private LocalDate departureDate;
    private double maxPrice;

    public SearchCriteria(String origin, String destination,
                          LocalDate departureDate, String airline) {
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.airline = airline;
        this.maxPrice = Double.MAX_VALUE;
    }

    public boolean matches(Flight f) {
        if (f == null) return false;
        boolean ok = true;
        if (origin != null && !origin.isBlank())
            ok &= f.getOrigin().equalsIgnoreCase(origin);
        if (destination != null && !destination.isBlank())
            ok &= f.getDestination().equalsIgnoreCase(destination);
        if (departureDate != null)
            ok &= f.getDepartureTime().toLocalDate().equals(departureDate);
        if (airline != null && !airline.isBlank() && !airline.equalsIgnoreCase("Any"))
            ok &= f.getAirline().equalsIgnoreCase(airline);
        if (f.getPrice() > maxPrice) ok = false;
        return ok;
    }

    public boolean isValid() {
        return origin != null && !origin.isBlank()
            && destination != null && !destination.isBlank()
            && departureDate != null
            && !departureDate.isBefore(LocalDate.now());
    }

    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getAirline() { return airline; }
    public LocalDate getDepartureDate() { return departureDate; }
    public double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(double p) { this.maxPrice = p; }
}