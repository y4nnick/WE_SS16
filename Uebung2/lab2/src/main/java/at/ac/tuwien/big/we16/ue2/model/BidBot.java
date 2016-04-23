package at.ac.tuwien.big.we16.ue2.model;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Every 10 seconds, this bot automatically makes a higher bid on each product with a probability of 30%.
 * Created by mstrasser on 4/22/16.
 */
public class BidBot implements Runnable {

    // Declares the amount by which the current bid shall be raised.
    private static final double RAISE_BY = 1.0;
    private ConcurrentHashMap<Integer, Product> products;
    private User u;

    /**
     * Constructor for BidBot
     * @param products List of all products with running auctions.
     * @param u The user assigned to the BidBot
     */
    public BidBot(ConcurrentHashMap<Integer, Product> products, User u) {
        this.products = products;
        this.u = u;
    }

    @Override
    /**
     * Goes through all products and makes a higher bid on each one with a probability of 30%.
     */
    public void run() {
        Random rand = new Random();
        Bid newBid;

        for(Product p:this.products.values()) {
            if(p.isRunning() && rand.nextInt(3) == 2) {
                newBid = new Bid(this.u, p.getPrice() + this.RAISE_BY);
                p.addBid(newBid);
            }
        }
    }

}
