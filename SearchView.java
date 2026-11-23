package flightres;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class SearchView extends JFrame {
    private final SearchController controller;
    private final FlightDAO flightDAO;
    private final BookingController bookingController;

    private final JTextField originField      = new JTextField(15);
    private final JTextField destinationField = new JTextField(15);
    private final JSpinner dateSpinner;
    private final JComboBox<String> airlineCombo = new JComboBox<>();
    private final JLabel errorLabel = new JLabel(" ", SwingConstants.LEFT);

    public SearchView(SearchController controller, FlightDAO flightDAO,
                      BookingController bookingController) {
        super("Search Flights");
        this.controller = controller;
        this.flightDAO = flightDAO;
        this.bookingController = bookingController;

        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(model);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 320);
        setLocationRelativeTo(null);
    }

    public void displaySearchForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Origin:"), g);
        g.gridx = 1; form.add(originField, g);

        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Destination:"), g);
        g.gridx = 1; form.add(destinationField, g);

        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Date:"), g);
        g.gridx = 1; form.add(dateSpinner, g);

        g.gridx = 0; g.gridy = 3; form.add(new JLabel("Airline:"), g);
        airlineCombo.addItem("Any");
        try {
            List<String> airlines = flightDAO.getAirlines();
            if (airlines != null) for (String a : airlines) airlineCombo.addItem(a);
        } catch (Exception ex) {
            System.err.println("Airline load failed: " + ex.getMessage());
        }
        g.gridx = 1; form.add(airlineCombo, g);

        JButton searchBtn = new JButton("Search");
        JButton clearBtn  = new JButton("Clear");
        searchBtn.addActionListener(e -> submitSearch());
        clearBtn.addActionListener(e -> {
            originField.setText(""); destinationField.setText("");
            airlineCombo.setSelectedIndex(0); errorLabel.setText(" ");
        });
        JPanel buttons = new JPanel();
        buttons.add(searchBtn);
        buttons.add(clearBtn);
        g.gridx = 0; g.gridy = 4; g.gridwidth = 2; form.add(buttons, g);

        errorLabel.setForeground(Color.RED);
        g.gridy = 5; form.add(errorLabel, g);

        setContentPane(form);
        setVisible(true);
        originField.requestFocusInWindow();
    }

    private boolean validateInput() {
        if (originField.getText().isBlank())      { displayError("Origin is required."); return false; }
        if (destinationField.getText().isBlank()) { displayError("Destination is required."); return false; }
        if (originField.getText().trim().equalsIgnoreCase(destinationField.getText().trim())) {
            displayError("Origin and destination must differ."); return false;
        }
        if (getSelectedDate().isBefore(LocalDate.now())) {
            displayError("Date cannot be in the past."); return false;
        }
        return true;
    }

    private LocalDate getSelectedDate() {
        Date d = (Date) dateSpinner.getValue();
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void submitSearch() {
        if (!validateInput()) return;
        String airline = (String) airlineCombo.getSelectedItem();
        SearchCriteria criteria = new SearchCriteria(
            originField.getText().trim(),
            destinationField.getText().trim(),
            getSelectedDate(),
            "Any".equals(airline) ? null : airline);

        List<Flight> results;
        try {
            results = controller.searchFlights(criteria);
        } catch (Exception ex) {
            displayError("Database error: " + ex.getMessage());
            return;
        }

        errorLabel.setText(" ");
        new SearchResultsView(results, controller, bookingController).displayResults(results);
    }

    public void displayError(String message) { errorLabel.setText(message); }
}