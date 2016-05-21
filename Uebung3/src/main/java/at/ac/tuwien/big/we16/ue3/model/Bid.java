package at.ac.tuwien.big.we16.ue3.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Bid {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private int amount;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    public Bid(){}

    public Bid(int centAmount, User user) {
        amount = centAmount;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public float getConvertedAmount() {
        float convertedAmount = (float)this.amount;
        return convertedAmount / 100;
    }

    public User getUser() {
        return user;
    }

    public boolean isBy(User user) {
        return this.user.equals(user);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
