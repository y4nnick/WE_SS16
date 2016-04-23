package at.ac.tuwien.big.we16.ue2.service;

import at.ac.tuwien.big.we16.ue2.model.Bid;
import at.ac.tuwien.big.we16.ue2.model.BidBot;
import at.ac.tuwien.big.we16.ue2.model.User;
import at.ac.tuwien.big.we16.ue2.productdata.UserHandler;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotifierService {
    private static Map<Session, HttpSession> clients = new ConcurrentHashMap<>();
    private static Map<User,HttpSession> loggedIn = new ConcurrentHashMap<>();

    private final ScheduledExecutorService executor;

    /**
     * TODO: HttpSession direkt zum User speichern
     */

    public NotifierService() {
        // Use the scheduled executor to regularly check for recently expired auctions
        // and send a notification to all relevant users.
        this.executor = Executors.newSingleThreadScheduledExecutor();
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
        System.out.println("NotifierService: register");
        UserHandler.generateUser();

        clients.put(socketSession, httpSession);
        //sendMessageToUsers(null, "Hallo test");
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

    public static void sendNewBidNotificaiton(Bid bid){

        ArrayList<Session> sessions = NotifierService.getSessionsFromUsers(UserHandler.getLoggedInUsers());

        //Build message
        String message = "new bid";

        //Send to users

    }

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

    public void sendMessageToUsers(ArrayList<User> users, String message){

        for( Session s :clients.keySet()){
            try{
                s.getBasicRemote().sendText(message);
                System.out.println("Message send");
            }catch (Exception e){
                System.out.println("Error while sending message to user: " + e.getMessage());
            }
        }


    }
}
