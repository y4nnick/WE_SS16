package at.ac.tuwien.big.we16.ue2.model;

/**
 * Created by mstrasser on 4/12/16.
 */
public class Bid {
    public Bid(User u, float price) {
        this.user = u;
        this.price = price;
    }

    private User user;
    private float price;

    public User getUser() { return this.user; }
    public float getPrice() { return this.price; }
}
