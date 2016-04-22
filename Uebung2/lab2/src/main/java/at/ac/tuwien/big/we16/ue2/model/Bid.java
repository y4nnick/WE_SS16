package at.ac.tuwien.big.we16.ue2.model;

/**
 * Created by mstrasser on 4/12/16.
 */
public class Bid {
    public Bid(User u, double price) {
        this.user = u;
        this.price = price;
    }

    private Product product;
    private User user;
    private double price;

    public User getUser() { return this.user; }
    public double getPrice() { return this.price; }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
