package at.ac.tuwien.big.we16.ue2.service;

import at.ac.tuwien.big.we16.ue2.model.Bid;
import at.ac.tuwien.big.we16.ue2.model.BidBot;
import at.ac.tuwien.big.we16.ue2.model.Product;
import at.ac.tuwien.big.we16.ue2.model.User;
import at.ac.tuwien.big.we16.ue2.productdata.JSONDataLoader;
import at.ac.tuwien.big.we16.ue2.productdata.UserHandler;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.awt.image.AreaAveragingScaleFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotifierService {

    private static Boolean DEBUG = true;

    //Message Type Enum
    private enum MSG_TYPES {
        NEW_BID("NEW_BID"), NEW_HIGHEST_BID("NEW_HIGHEST_BID"), AUCTION_EXPIRED("AUCTION_EXPIRED");
        private final String value;

        MSG_TYPES(String val){
            this.value = val;
        }

        @Override
        public String toString(){
            return this.value;
        }
    }

    private static Map<Session, HttpSession> clients = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor;

    public NotifierService() {
        // Use the scheduled executor to regularly check for recently expired auctions
        // and send a notification to all relevant users.

        this.executor = Executors.newSingleThreadScheduledExecutor();

        ConcurrentHashMap<Integer, Product> products = JSONDataLoader.getProducts();
        this.executor.scheduleAtFixedRate(new AuctionListener(products),0,1, TimeUnit.SECONDS);
    }

    /**
     * This method is used by the WebSocket endpoint to save a reference to all
     * connected users. A list of connections is needed so that the users can be
     * notified about events like new bids and expired auctions (see
     * assignment). We need the socket session so that we can push data to the
     * client. We need the HTTP session to find out which user is currently
     * logged in in the browser that opened the socket connection.
     */
    public void register(Session socketSession, HttpSession httpSession) {
        if(DEBUG)System.out.println("Notifier Service in register");
        //UserHandler.generateUser();

        //Make sure that no client gets registerd twice
        for(HttpSession httpSession1 : clients.values()){
            if(httpSession1.equals(httpSession)){
                return;
            }
        }

        clients.put(socketSession, httpSession);
    }

    public void unregister(Session userSession) {
        clients.remove(userSession);
    }

    /**
     * Call this method from your servlet's shutdown listener to cleanly
     * shutdown the thread when the application stops.
     * 
     * See http://www.deadcoderising.com/execute-code-on-webapp-startup-and-shutdown-using-servletcontextlistener/
     */
    public void stop() {
        this.executor.shutdown();
    }


    /**
     * Sends a new highest Bid notification to the surpassed bidder
     * @param surpassedBidder the surpassed bidder
     */
    public static void sendNewHighestBidNotification(User surpassedBidder){

        try{

            //Get Session from surpassed bidder
            ArrayList<User> user = new ArrayList();
            user.add(surpassedBidder);
            ArrayList<Session> session = NotifierService.getSessionsFromUsers(user);

            //Get parameters
            double newBalance = surpassedBidder.getBalance();

            //Build Json Object
            JsonObject json = new JsonObject();
            json.addProperty("msgType",MSG_TYPES.NEW_HIGHEST_BID+"");
            json.addProperty("balance",newBalance);

            //Send
            sendJsonToSessions(json,session);

        }catch (Exception e){
            printException(e);
        }
    }

    /**
     * Sends a auction expired Notification to all users
     * @param product the product to which the auction was expired
     */
    public static void sendAuctionExpiredNotification(Product product){

        try{

            //Get logged in Users
            ArrayList<User> loggedInUsers = UserHandler.getLoggedInUsers();

            //Parameter
            int productID = product.getID();
            double balance;
            int runningAuctions, lostAuctions, wonAuctions;

            for(User u : loggedInUsers){

                //Get session from user
                ArrayList<User> user = new ArrayList<>();
                user.add(u);
                ArrayList<Session> session = NotifierService.getSessionsFromUsers(user);

                // Check if user has placed a bid on the product and update statistics accordingly
                if(product.isBidding(u)) {
                    Bid top = product.getTopBid();

                    if(top.getUser().getId() == u.getId())
                        u.addWonAuction(top);
                    else {
                        Bid last = product.getLastBidOf(u);
                        u.addLostAuction(last);
                        u.setBalance(u.getBalance() + last.getPrice());
                    }
                }

                //Get Parameters
                balance = u.getBalance();
                runningAuctions = u.getRunningAuctions();
                lostAuctions = u.getLostAuctions();
                wonAuctions = u.getWonAuctions();

                //Build Json Object
                JsonObject json = new JsonObject();
                json.addProperty("msgType",MSG_TYPES.AUCTION_EXPIRED+"");
                json.addProperty("product",productID);
                json.addProperty("balance",balance);
                json.addProperty("running",runningAuctions);
                json.addProperty("lost",lostAuctions);
                json.addProperty("won",wonAuctions);

                //Send
                sendJsonToSessions(json,session);

            }

        }catch (Exception e){
            printException(e);
        }
    }

    /**
     * Sends a new bid Notification to all logged in clients
     * @param bid the new bid
     */
    public static void sendNewBidNotification(Bid bid){

        try{

            //Get Sessions from logged in users
            ArrayList<User> loggedInUsers = UserHandler.getLoggedInUsers();
            ArrayList<Session> sessions = NotifierService.getSessionsFromUsers(loggedInUsers);

            //Get Parameters
            Double price = bid.getPrice();
            String bidder = (bid.getUser() != null)?bid.getUser().getName():"User of bid is null";
            Integer productID = bid.getProduct().getID();

            //Build Json Object
            JsonObject json = new JsonObject();
            json.addProperty("msgType",MSG_TYPES.NEW_BID+"");
            json.addProperty("price",price);
            json.addProperty("bidder",bidder);
            json.addProperty("product",productID);

            //Send
            sendJsonToSessions(json,sessions);

        }catch (Exception e){
            printException(e);
        }
    }

    /**
     * Gets the Sessions from the given users
     * @param users the users
     * @return the sessions from the users
     */
    private static ArrayList<Session> getSessionsFromUsers(ArrayList<User> users){
        ArrayList<Session> sessions = new ArrayList<>();

        for(User u : users){
            for (Session session : clients.keySet()) {
                HttpSession httpSession = clients.get(session);
                if(u.getHttpSession().equals(httpSession)){
                    sessions.add(session);
                }
            }
        }

        return sessions;
    }

    /**
     * Sends the given json-object to the given sessions
     * @param json the json-Object
     * @param sessions the session
     */
    private static void sendJsonToSessions(JsonObject json, ArrayList<Session> sessions){
        for( Session s :sessions){
            synchronized (s) {
                try{
                    if(DEBUG)System.out.println("Send to websocket #"+s.getId()+" | Message: " + json);
                    s.getBasicRemote().sendText(json.toString());
                }catch (Exception e){
                    printException(e);
                }

            }

        }
    }

    /**
     * Prints the given exception
     * @param e the exception
     */
    private static void printException(Exception e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionDetails = sw.toString();
        System.err.println(exceptionDetails);
    }
}
