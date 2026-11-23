package flightres;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerId;
    private String firstName, lastName, email, phone, username, passwordHash, address;
    private List<PromotionNews> promotions = new ArrayList<>();

    public Customer(int customerId, String firstName, String lastName,
                    String email, String phone, String username,
                    String passwordHash, String address) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.passwordHash = passwordHash;
        this.address = address;
    }

    public void updateProfile(String email, String phone, String address) {
        this.email = email; this.phone = phone; this.address = address;
    }

    public void receivePromotion(PromotionNews news) { promotions.add(news); }

    public List<PromotionNews> getPromotions() { return promotions; }
    public int getCustomerId() { return customerId; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getUsername() { return username; }
    public String getAddress() { return address; }
}