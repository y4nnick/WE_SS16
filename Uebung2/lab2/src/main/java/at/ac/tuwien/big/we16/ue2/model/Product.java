package at.ac.tuwien.big.we16.ue2.model;

import java.util.Stack;

/**
 * Created by mstrasser on 4/12/16.
 */
public class Product {
    private String id;
    private String name;
    private String img;
    private int year;
    private Bid highestBid = null;
    private Stack<Bid> bids = new Stack<Bid>();

    public Product(String ID, String name, String img, int year) {
        this.id = ID;
        this.name = name;
        this.img = img;
        this.year = year;
    }

    public Bid getTopBid() { return this.highestBid; }

    public void addBid(Bid b) {
        this.highestBid = b;
        this.bids.push(b);
    }

    public void addBid(User u, float price) {
        Bid b = new Bid(u, price);

        this.highestBid = b;
        this.bids.push(b);
    }

    public float getPrice() {
        if(this.highestBid == null)
            return 0;

        return this.highestBid.getPrice();
    }

    public int getYear() { return this.year; }
    public void setYear(int year) { this.year = year; }

    public String getImg() { return this.img; }
    public void setImg() { this.img = img; }

    public String getName() { return this.name; }
    public String getID() { return this.id; }
}
