package flightres;

public class LoginController {
    private final UserDAO userDAO;

    public LoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User authenticate(String username, String password, String role) {
        if (username == null || password == null || role == null) return null;
        User u = userDAO.findByUsernameAndRole(username.trim(), role);
        if (u == null) return null;
        return u.getPasswordHash().equals(password) ? u : null;
    }

    public String dashboardFor(User u) {
        if (u == null) return null;
        switch (u.getRole().toUpperCase()) {
            case "CUSTOMER":
            case "AGENT":  return "MAIN";
            case "ADMIN":  return "ADMIN";
            default:       return null;
        }
    }

    public boolean hasPermission(User u, String action) {
        if (u == null || action == null) return false;
        String role = u.getRole().toUpperCase();
        switch (action) {
            case "SEARCH":           return role.equals("CUSTOMER") || role.equals("AGENT");
            case "BOOK":             return role.equals("CUSTOMER") || role.equals("AGENT");
            case "MANAGE_FLIGHTS":   return role.equals("ADMIN");
            case "MANAGE_CUSTOMERS": return role.equals("AGENT") || role.equals("ADMIN");
            default:                 return false;
        }
    }
}