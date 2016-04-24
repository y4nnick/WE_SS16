package at.ac.tuwien.big.we16.ue2.model;

import at.ac.tuwien.big.we16.ue2.productdata.UserHandler;
import at.ac.tuwien.big.we16.ue2.service.NotifierService;

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

    /**
     * Constructor for BidBot
     * @param products List of all products with running auctions.
     */
    public BidBot(ConcurrentHashMap<Integer, Product> products) {
        this.products = products;
    }

    @Override
    /**
     * Goes through all products and makes a higher bid on each one with a probability of 30%.
     */
    public void run() {
        Random rand = new Random();
        Bid newBid, oldBid;

        for(Product p:this.products.values()) {
            if(p.isRunning() && rand.nextInt(3) == 2) {

                newBid = new Bid(p, UserHandler.getBidBot(), p.getPrice() + this.RAISE_BY);

                //Get old highest bidder
                User oldHighestUser = (p.getTopBid() != null)?p.getTopBid().getUser():null;

                p.addBid(newBid);

                //Send new Bid notification to all users
                NotifierService.sendNewBidNotification(newBid);

                //Send new highest Bid to surpassed user
                if(oldHighestUser != null){
                    NotifierService.sendNewHighestBidNotification(p.getLastBidOf(oldHighestUser), oldHighestUser);
                }
            }
        }
    }

}
