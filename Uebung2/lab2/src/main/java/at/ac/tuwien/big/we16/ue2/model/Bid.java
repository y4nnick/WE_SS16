package at.ac.tuwien.big.we16.ue2.model;

/**
 * Created by mstrasser on 4/12/16.
 */
public class Bid {
    private Product product;
    private User user;
    private double price;

    public Bid(Product product, User user, double price) {
        this.product = product;
        this.user = user;
        this.price = price;
    }

    public User getUser() { return this.user; }

    public double getPrice() { return this.price; }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPrice(double newPrice) {this.price = newPrice; }
}
