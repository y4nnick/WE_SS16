package at.ac.tuwien.big.we16.ue2.service;

import at.ac.tuwien.big.we16.ue2.model.Product;
import at.ac.tuwien.big.we16.ue2.productdata.JSONDataLoader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mstrasser on 4/12/16.
 */
@WebServlet(name="Details", urlPatterns = { "/details"})
public class DetailViewServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");

        int product_id = Integer.parseInt(request.getParameter("id").toString());
        Product product = JSONDataLoader.getById(product_id);
        request.setAttribute("product", product);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/details.jsp");
        dispatcher.forward(request, response);
    }
}
