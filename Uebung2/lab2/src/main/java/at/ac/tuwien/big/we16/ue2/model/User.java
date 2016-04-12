package at.ac.tuwien.big.we16.ue2.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mstrasser on 4/12/16.
 */
public class User {
    private String name;
    private int balance;
    private List<Bid> bids_running = new LinkedList<Bid>();
    private List<Bid> bids_won = new LinkedList<Bid>();
    private List<Bid> bids_lost = new LinkedList<Bid>();

    public String getName() {
        return this.name;
    }

    public int getBalance() {
        return this.balance;
    }

    public int getRunningAuctions() {
        return this.bids_running.size();
    }

    public int getWonAuctions() {
        return this.bids_won.size();
    }

    public int getLostAuctions() {
        return this.bids_lost.size();
    }
}
