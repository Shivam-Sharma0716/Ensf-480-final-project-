package flightres;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public final class AppContext {
    private static Connection conn;
    private static FlightDAO flightDAO;
    private static UserDAO userDAO;
    private static BookingController bookingController;

    public static synchronized void init() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:flights.db");
        } catch (SQLException e) {
            throw new RuntimeException("Cannot open flights.db", e);
        }
        // TEMPORARY stubs — replace when M3 delivers real DAOs:
        flightDAO = criteria -> List.of();
        userDAO   = (u, r) -> null;
        bookingController = new BookingController();
    }

    public static FlightDAO flightDAO() { return flightDAO; }
    public static UserDAO userDAO() { return userDAO; }
    public static BookingController bookingController() { return bookingController; }
    public static Connection conn() { return conn; }

    public static void main(String[] args) {
        init();
        LoginController lc = new LoginController(userDAO);
        SwingUtilities.invokeLater(() -> new LoginView(lc).displayRoleSelection());
    }
}