package flightres;

import javax.swing.*;
import java.awt.*;

public class MainDashboardView extends JFrame {
    private final User currentUser;
    private final LoginController loginController;
    private final FlightDAO flightDAO;
    private final BookingController bookingController;

    public MainDashboardView(User currentUser, LoginController loginController) {
        this(currentUser, loginController, AppContext.flightDAO(), AppContext.bookingController());
    }

    public MainDashboardView(User currentUser, LoginController loginController,
                             FlightDAO flightDAO, BookingController bookingController) {
        super("Flight Reservation System — Dashboard");
        this.currentUser = currentUser;
        this.loginController = loginController;
        this.flightDAO = flightDAO;
        this.bookingController = bookingController;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 340);
        setLocationRelativeTo(null);
    }

    public void displayDashboard(String role) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JLabel welcome = new JLabel("Welcome, " + currentUser.getFullName()
                + " (" + role + ")", SwingConstants.CENTER);
        welcome.setFont(welcome.getFont().deriveFont(Font.BOLD, 16f));
        welcome.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        panel.add(welcome, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        JButton searchBtn = new JButton("Search Flights");
        searchBtn.addActionListener(e -> navigateToSearch());

        JButton historyBtn = new JButton("View My Bookings");
        historyBtn.addActionListener(e -> navigateToBookingHistory());

        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.addActionListener(e -> logout());

        if (loginController.hasPermission(currentUser, "SEARCH")) buttons.add(searchBtn);
        buttons.add(historyBtn);
        buttons.add(logoutBtn);

        panel.add(buttons, BorderLayout.CENTER);
        setContentPane(panel);
        setVisible(true);
    }

    private void navigateToSearch() {
        if (!loginController.hasPermission(currentUser, "SEARCH")) {
            JOptionPane.showMessageDialog(this, "Your role is not allowed to search.");
            return;
        }
        SearchController sc = new SearchController(flightDAO);
        new SearchView(sc, flightDAO, bookingController).displaySearchForm();
    }

    private void navigateToBookingHistory() {
        JOptionPane.showMessageDialog(this,
            "Booking history screen is owned by Member 2 — plug in BookingHistoryView here.");
    }

    private void logout() {
        dispose();
        new LoginView(loginController).displayRoleSelection();
    }
}