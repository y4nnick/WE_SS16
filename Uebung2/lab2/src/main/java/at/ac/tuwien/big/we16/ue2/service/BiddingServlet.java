package at.ac.tuwien.big.we16.ue2.service;

import at.ac.tuwien.big.we16.ue2.model.Bid;
import at.ac.tuwien.big.we16.ue2.model.Product;
import at.ac.tuwien.big.we16.ue2.model.User;
import at.ac.tuwien.big.we16.ue2.productdata.JSONDataLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.tools.corba.se.idl.constExpr.Not;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "Bidding", urlPatterns = {"/bidding"})
public class BiddingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);

        float newPrice = data.get("newPrice").getAsFloat();
        int id = data.get("id").getAsInt();

        Product product = JSONDataLoader.getById(id);
        float highestPrice = (float) product.getPrice();

        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("currentSessionUser");

        User oldHighestBidder = null;
        Bid newBid = null;

        if (newPrice > highestPrice) {
            // Check for existing bid
            List<Bid> bidList = user.getRunningActionsList();
            Optional<Bid> existingBid = bidList
                    .stream()
                    .filter(bid -> bid.getProduct().getID() == id)
                    .findAny();

            //set new balance
            double balanceUpdated = user.getBalance() - newPrice;
            if (existingBid.isPresent()) {
                balanceUpdated += existingBid.get().getPrice();
            }
            if (balanceUpdated > 0) {
                user.setBalance(balanceUpdated);
            } else {
                response.setStatus(409);
                response.getWriter().write("Nicht genug Geld.");
                return;
            }

            oldHighestBidder = product.getTopBid().getUser();

            //set new highest bid
            product.addBid(user, newPrice);

            newBid = product.getTopBid();


            //add new auction to running auctions
            if (existingBid.isPresent()) {
                existingBid.get().setPrice(newPrice);
            } else {
                user.getRunningActionsList().add(new Bid(product, user, newPrice));
            }
        } else {
            response.setStatus(409);
            response.getWriter().write("Gebot ist nicht hoch genug.");
            return;
        }

        //Send notification to surpassed user
        NotifierService.sendNewHighestBidNotification(oldHighestBidder);

        //Send notification to all users about the new Bid
        NotifierService.sendNewBidNotification(newBid);

        JsonObject json = new JsonObject();
        json.addProperty("balance", user.getBalance());
        json.addProperty("running", user.getRunningAuctions());

        json.addProperty("price", Double.toString(product.getPrice()));

        response.getWriter().write(json.toString());
    }
}
