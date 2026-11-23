package flightres;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SearchResultsView extends JFrame {
    private final List<Flight> flights;
    private final SearchController controller;
    private final BookingController bookingController;
    private final JTable table;
    private final DefaultTableModel model;

    public SearchResultsView(List<Flight> flights, SearchController controller,
                             BookingController bookingController) {
        super("Search Results");
        this.flights = flights;
        this.controller = controller;
        this.bookingController = bookingController;

        String[] cols = {"Flight #", "Airline", "Origin", "Destination",
                         "Departure", "Arrival", "Duration", "Price", "Seats"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(820, 420);
        setLocationRelativeTo(null);
    }

    public void displayResults(List<Flight> flights) {
        model.setRowCount(0);
        if (flights == null || flights.isEmpty()) { showNoResultsMessage(); return; }
        for (Flight f : flights) renderFlightRow(f);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton backBtn = new JButton("Back to Search");
        backBtn.addActionListener(e -> dispose());
        JButton detailsBtn = new JButton("View Details");
        detailsBtn.addActionListener(e -> handleFlightSelection());

        JPanel buttons = new JPanel();
        buttons.add(detailsBtn);
        buttons.add(backBtn);
        root.add(buttons, BorderLayout.SOUTH);

        setContentPane(root);
        setVisible(true);
    }

    private void renderFlightRow(Flight f) {
        long mins = f.getDuration().toMinutes();
        String dur = String.format("%dh %02dm", mins / 60, mins % 60);
        model.addRow(new Object[]{
            f.getFlightNumber(), f.getAirline(), f.getOrigin(), f.getDestination(),
            f.getDepartureTime().toString().replace('T', ' '),
            f.getArrivalTime().toString().replace('T', ' '),
            dur, String.format("%.2f", f.getPrice()), f.getAvailableSeats()
        });
    }

    private void handleFlightSelection() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a flight first."); return; }
        Flight f = flights.get(row);
        Flight full = controller.getFlightDetails(f.getFlightId());
        new FlightDetailsView(full != null ? full : f, bookingController).displayFlightDetails();
    }

    public void showNoResultsMessage() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        JLabel msg = new JLabel("No flights found. Try different criteria.", SwingConstants.CENTER);
        msg.setFont(msg.getFont().deriveFont(Font.BOLD, 14f));
        root.add(msg, BorderLayout.CENTER);
        JButton backBtn = new JButton("Back to Search");
        backBtn.addActionListener(e -> dispose());
        JPanel b = new JPanel(); b.add(backBtn);
        root.add(b, BorderLayout.SOUTH);
        setContentPane(root);
        setVisible(true);
    }
}