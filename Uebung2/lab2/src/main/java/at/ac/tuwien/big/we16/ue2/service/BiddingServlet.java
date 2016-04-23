package at.ac.tuwien.big.we16.ue2.service;

import at.ac.tuwien.big.we16.ue2.model.Product;
import at.ac.tuwien.big.we16.ue2.model.User;
import at.ac.tuwien.big.we16.ue2.productdata.JSONDataLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

        if (newPrice > highestPrice) {
            product.addBid(user, newPrice);
        } else {
            response.setStatus(409);
            response.getWriter().write("Gebot ist nicht hoch genug.");
            return;
        }
        response.getWriter().write(Double.toString(product.getPrice()));
    }
}
