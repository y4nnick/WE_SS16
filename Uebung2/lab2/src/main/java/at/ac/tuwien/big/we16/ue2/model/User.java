package at.ac.tuwien.big.we16.ue2.model;

import at.ac.tuwien.big.we16.ue2.productdata.UserHandler;

import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mstrasser on 4/12/16.
 */
public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private double balance;
    private List<Bid> bids_running = new LinkedList<Bid>();
    private List<Bid> bids_won = new LinkedList<Bid>();
    private List<Bid> bids_lost = new LinkedList<Bid>();
    private HttpSession httpSession = null; //TODO set null on logout

    public User(int id, String name,String email,String password,int balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.email = email;
        this.password = UserHandler.saltEmailAndPassword(email,password);
    }

    public String getName() {
        return this.name;
    }

    public double getBalance() {
        return this.balance;
    }

    /**
     * Adds a new bid to the list of running auctions of this user.
     * @param b The new bid.
     */
    public void addRunningAuction(Bid b) {
        this.bids_running.add(b);
    }
    public int getRunningAuctions() {
        return this.bids_running.size();
    }

    /**
     * Adds a running bid to the list of won auctions and removes it from the list of running auctions.
     * @param b The running bid
     */
    public void addWonAuction(Bid b) {
        this.bids_running.remove(b);
        this.bids_won.add(b);
    }
    public int getWonAuctions() {
        return this.bids_won.size();
    }

    /**
     * Adds a running bid to the list of lost auctions and removes it from the list of running auctions.
     * @param b The running bid
     */
    public void addLostAuction(Bid b) {
        this.bids_running.remove(b);
        this.bids_lost.add(b);
    }
    public int getLostAuctions() {
        return this.bids_lost.size();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public Boolean isLoggedIn(){
        return this.httpSession != null;
    }

    public void setBalance(double newBalance) {this.balance = newBalance; }

    public List<Bid> getRunningActionsList(){return bids_running; }
}
