package at.ac.tuwien.big.we16.ue2.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

/**
 * Created by mstrasser on 4/12/16.
 */
public class Product {
    private Integer id;
    private String name;
    private String img;
    private int year;
    private Bid highestBid = null;
    private Stack<Bid> bids = new Stack<Bid>();

    private Date auctionEnd;
    private boolean isRunning = true;

    public Product(Integer ID, String name, String img, int year) {
        this.id = ID;
        this.name = name;
        this.img = img;
        this.year = year;
    }

    public String getHighestBidName(){

        if(this.highestBid == null || this.highestBid.getUser() == null){
            return "Kein Gebot";
        }

        return this.highestBid.getUser().getName();
    }

    public Boolean hasBid(){
        return (this.highestBid != null);
    }

    public Bid getTopBid() { return this.highestBid; }

    public void addBid(Bid b) {
        this.highestBid = b;
        this.bids.push(b);
    }

    public void addBid(User u, float price) {
        Bid b = new Bid(u, price);
        addBid(b);
    }

    public double getPrice() {
        if(this.highestBid == null)
            return 0;

        return this.highestBid.getPrice();
    }

    public int getYear() { return this.year; }
    public void setYear(int year) { this.year = year; }

    public String getImg() { return this.img; }
    public void setImg() { this.img = img; }

    public String getName() { return this.name; }
    public Integer getID() { return this.id; }

    public Date getAuctionEnd() {
        return auctionEnd;
    }
    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    /**
     * Returns the Auction end date in the format YYYY,MM,dd,HH,mm,ss,SSS
     * @return the Auction end date in the format YYYY,MM,dd,HH,mm,ss,SSS
     */
    public String getAuctionEndString(){
        //2016,03,14,14,30,23,288
        // endTime = new Date(endTime[0],endTime[1]-1,endTime[2],endTime[3],endTime[4],endTime[5],endTime[6]);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY,MM,dd,HH,mm,ss,SSS");
        return sdf.format(auctionEnd);
    }

    public boolean isRunning() { return this.isRunning; }
    public void setRunning(boolean running) {
        isRunning = running;
    }
}
