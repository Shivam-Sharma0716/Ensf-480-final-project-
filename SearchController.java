package flightres;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchController {
    private final FlightDAO flightDAO;
    private SearchCriteria currentCriteria;
    private List<Flight> lastResults = new ArrayList<>();

    public SearchController(FlightDAO flightDAO) { this.flightDAO = flightDAO; }

    public List<Flight> searchFlights(SearchCriteria criteria) {
        this.currentCriteria = criteria;
        List<Flight> raw = flightDAO.findFlights(criteria);
        if (raw == null) raw = new ArrayList<>();
        List<Flight> available = filterAvailableFlights(raw);
        available.sort(Comparator.comparing(Flight::getDepartureTime));
        this.lastResults = available;
        return available;
    }

    public List<Flight> filterAvailableFlights(List<Flight> flights) {
        List<Flight> out = new ArrayList<>();
        if (flights == null) return out;
        for (Flight f : flights) if (f != null && f.hasAvailableSeats()) out.add(f);
        return out;
    }

    public Flight selectFlight(int flightId) {
        for (Flight f : lastResults) if (f.getFlightId() == flightId) return f;
        return null;
    }

    public Flight getFlightDetails(int flightId) { return flightDAO.findById(flightId); }

    public List<Flight> sortResults(SearchCriteria criteria, String by) {
        List<Flight> copy = new ArrayList<>(lastResults);
        if ("price".equalsIgnoreCase(by)) {
            copy.sort(Comparator.comparingDouble(Flight::getPrice));
        } else if ("duration".equalsIgnoreCase(by)) {
            copy.sort(Comparator.comparing(Flight::getDuration));
        } else {
            copy.sort(Comparator.comparing(Flight::getDepartureTime));
        }
        return copy;
    }

    public SearchCriteria getCurrentCriteria() { return currentCriteria; }
    public List<Flight> getLastResults() { return lastResults; }
}