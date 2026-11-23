package flightres;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final JComboBox<String> roleComboBox;
    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JLabel errorLabel = new JLabel(" ", SwingConstants.CENTER);
    private final LoginController controller;

    public LoginView(LoginController controller) {
        super("Flight Reservation System — Login");
        this.controller = controller;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Flight Reservation System", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Role:"), g);
        roleComboBox = new JComboBox<>(new String[]{"CUSTOMER", "AGENT", "ADMIN"});
        g.gridx = 1; form.add(roleComboBox, g);

        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Username:"), g);
        g.gridx = 1; form.add(usernameField, g);

        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Password:"), g);
        g.gridx = 1; form.add(passwordField, g);

        JButton loginButton = new JButton("Log In");
        JButton exitButton  = new JButton("Exit");
        loginButton.addActionListener(e -> submitCredentials());
        exitButton.addActionListener(e -> System.exit(0));

        JPanel buttons = new JPanel();
        buttons.add(loginButton);
        buttons.add(exitButton);

        g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
        form.add(buttons, g);

        errorLabel.setForeground(Color.RED);
        g.gridy = 4; form.add(errorLabel, g);

        add(form, BorderLayout.CENTER);
        getRootPane().setDefaultButton(loginButton);
    }

    public void displayRoleSelection() {
        setVisible(true);
        usernameField.requestFocusInWindow();
    }

    private void submitCredentials() {
        String role = (String) roleComboBox.getSelectedItem();
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user == null || user.isBlank() || pass == null || pass.isBlank()) {
            displayLoginError("Please enter both username and password.");
            return;
        }

        User authenticated = controller.authenticate(user, pass, role);
        if (authenticated == null) {
            displayLoginError("Invalid credentials for role " + role + ".");
            return;
        }

        errorLabel.setText(" ");
        dispose();
        String dash = controller.dashboardFor(authenticated);
        if ("MAIN".equals(dash)) {
            new MainDashboardView(authenticated, controller).displayDashboard(authenticated.getRole());
        } else if ("ADMIN".equals(dash)) {
            JOptionPane.showMessageDialog(null,
                "Admin dashboard is owned by Member 3 — plug in AdminDashboardView here.");
        } else {
            displayLoginError("Unknown role.");
        }
    }

    public void displayLoginError(String message) { errorLabel.setText(message); }
}