package flightres;

import javax.swing.*;
import java.awt.*;

public class FlightDetailsView extends JFrame {
    private final Flight selectedFlight;
    private final BookingController bookingController;

    public FlightDetailsView(Flight selectedFlight, BookingController bookingController) {
        super("Flight Details");
        this.selectedFlight = selectedFlight;
        this.bookingController = bookingController;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(460, 380);
        setLocationRelativeTo(null);
    }

    public void displayFlightDetails() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        JPanel info = new JPanel(new GridLayout(0, 2, 8, 8));
        info.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        addRow(info, "Flight Number:", selectedFlight.getFlightNumber());
        addRow(info, "Airline:",       selectedFlight.getAirline());
        addRow(info, "Origin:",        selectedFlight.getOrigin());
        addRow(info, "Destination:",   selectedFlight.getDestination());
        addRow(info, "Departure:",     selectedFlight.getDepartureTime().toString().replace('T', ' '));
        addRow(info, "Arrival:",       selectedFlight.getArrivalTime().toString().replace('T', ' '));
        long mins = selectedFlight.getDuration().toMinutes();
        addRow(info, "Duration:",      String.format("%dh %02dm", mins / 60, mins % 60));
        addRow(info, "Price:",         "$" + String.format("%.2f", selectedFlight.getPrice()));
        addRow(info, "Seats Available:", String.valueOf(selectedFlight.getAvailableSeats()));

        root.add(info, BorderLayout.CENTER);

        JButton bookBtn  = new JButton("Book This Flight");
        JButton closeBtn = new JButton("Back to Results");
        bookBtn.addActionListener(e -> navigateToBooking());
        closeBtn.addActionListener(e -> dispose());

        JPanel buttons = new JPanel();
        buttons.add(bookBtn);
        buttons.add(closeBtn);
        root.add(buttons, BorderLayout.SOUTH);

        if (!selectedFlight.hasAvailableSeats()) {
            bookBtn.setEnabled(false);
            bookBtn.setToolTipText("No seats available.");
        }

        setContentPane(root);
        setVisible(true);
    }

    private void addRow(JPanel p, String label, String value) {
        JLabel l = new JLabel(label);
        l.setFont(l.getFont().deriveFont(Font.BOLD));
        p.add(l);
        p.add(new JLabel(value));
    }

    private void navigateToBooking() {
        bookingController.startBooking(selectedFlight);
        dispose();
    }
}