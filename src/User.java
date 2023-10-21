import java.util.ArrayList;

/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class User {

    private final String username;
    private ArrayList<String> cart;

    public User(String username) {
        this.username = username;
        this.cart = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String setUsername() {
        return username;
    }

    public ArrayList<String> getCart() {
        return cart;
    }

    public void addToCart(String item) {
        cart.add(item);
    }
}
