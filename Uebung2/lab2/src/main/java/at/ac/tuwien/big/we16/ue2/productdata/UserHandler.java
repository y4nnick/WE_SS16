package at.ac.tuwien.big.we16.ue2.productdata;

import at.ac.tuwien.big.we16.ue2.model.User;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Yannick on Samstag23.04.16.
 */
public class UserHandler {

    private static Boolean DEBUG = true;

    //Stores all users of the system, the key-value is the id from the user
    private static ConcurrentHashMap<Integer,User> users = new ConcurrentHashMap<>();

    private static User bidBot = null;

    /**
     * Finds a user with the given email. Password checking is out of scope for this exercise.
     * The bot is not allowed to login (null will be returned)
     * @param email Email of the user
     * @param password Password of the User
     * @return The found user or null if not found or login by bot
     */
    public static User findUser(String email, String password){

        if(email == null || email.isEmpty()){
            if(DEBUG)System.err.println("email is empty");
            return null;
        }

        //Bot is not allowed to login
        if(email.equals(bidBot.getEmail())){
            if(DEBUG)System.err.println("Bot is not allowed to login");
            return null;
        }

        for(User u : users.values()){
            if(u.getEmail().equals(email)){
                return u;
            }
        }

        if(DEBUG)System.err.println("No user match");
        return null;
    }

    /**
     * Generates and stores the BidBot users and three additional test users for Edith, Michael and Yannick
     */
    public static void generateUser(){

        if(bidBot == null){
            bidBot = new User(1,"BidBot", "bot@bigbid.com", "secret1", Integer.MAX_VALUE);

            User userEdith =    new User(2, "Edith",   "edith@bigbid.at",   "secret2", 1500);
            User userMichael =  new User(3, "Michael", "michael@bigbid.at", "secret3", 1500);
            User userYannick =  new User(4, "Yannick", "yannick@bigbid.at", "secret4", 1500);

            users.put(userEdith.getId(),userEdith);
            users.put(userMichael.getId(),userMichael);
            users.put(userYannick.getId(),userYannick);
        }

    }

    /**
     * Generates a salted Hash from Email and Password
     * @param email the Email
     * @param password the Password
     * @return the salted string or the password in cleartext if SHA-256 is not available
     */
    public static String saltEmailAndPassword(String email, String password){
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update((email + password).getBytes());
            String hash = new String(messageDigest.digest());
            return hash;
        }catch (Exception e){
            System.out.println("SHA-256 not available");
            return password;
        }
    }

    /**
     * Returns the User for the Bid Bot
     * @return the Bid Bot User
     */
    public static User getBidBot(){
        return bidBot;
    }

    /**
     * Returns all users who are currently logged in
     * @return all logged in users
     */
    public static ArrayList<User> getLoggedInUsers(){
        ArrayList<User> loggedInUsers = new ArrayList<>();

        for(User u : users.values()){
            if(u.isLoggedIn()){
                loggedInUsers.add(u);
            }
        }

        return loggedInUsers;
    }
}
