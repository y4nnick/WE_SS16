package at.ac.tuwien.big.we16.ue2.service;

/**
 * Created by Yannick on Samstag23.04.16.
 */

import at.ac.tuwien.big.we16.ue2.model.User;
import at.ac.tuwien.big.we16.ue2.productdata.UserHandler;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(name="Login", urlPatterns = { "/login"})
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(true);
        Object obj = session.getAttribute("currentSessionUser");
        if(obj == null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/login.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("/overview");
        }
    }

    // @TODO: Notify user if login failed
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {

        try
        {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            User user = UserHandler.findUser(email, password);

            if(user == null){
                System.out.println("User not found");
                response.sendRedirect("login");
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("currentSessionUser",user);
            user.setHttpSession(session);

            response.sendRedirect("overview");
        }
        catch (Throwable theException)
        {

            System.out.println(theException.getMessage());
            System.out.println(theException.getStackTrace());
            response.sendRedirect("login");
        }
    }
}