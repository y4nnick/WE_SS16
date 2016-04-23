package at.ac.tuwien.big.we16.ue2.service;

import at.ac.tuwien.big.we16.ue2.model.Product;

import javax.print.attribute.standard.PrinterURI;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Yannick on Samstag23.04.16.
 */
public class AuctionListener implements Runnable {

    private ConcurrentHashMap<Integer, Product> products;
    private static Boolean DEBUG = false;

    public AuctionListener(ConcurrentHashMap<Integer, Product> products){
        this.products = products;
    }

    @Override
    /**
     * Goes through all products and checks if the auction of them is expired
     */
    public void run() {
        if(DEBUG)System.out.println("Auction Listener: Start checking");

        Date current = new Date();
        for(Product p : products.values()){
            if(p.isRunning() && current.getTime() >= p.getAuctionEnd().getTime()){
                p.setRunning(false);
                NotifierService.sendAuctionExpiredNotification(p);
            }
        }
    }

}
