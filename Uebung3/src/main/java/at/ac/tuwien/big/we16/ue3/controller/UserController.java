package at.ac.tuwien.big.we16.ue3.controller;

import at.ac.tuwien.big.we16.ue3.exception.ValidationException;
import at.ac.tuwien.big.we16.ue3.model.User;
import at.ac.tuwien.big.we16.ue3.service.AuthService;
import at.ac.tuwien.big.we16.ue3.service.UserService;
import com.sun.prism.shader.DrawCircle_Color_AlphaTest_Loader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    public void getRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (this.authService.isLoggedIn(request.getSession())) {
            response.sendRedirect("/");
            return;
        }
        request.getRequestDispatcher("/views/registration.jsp").forward(request, response);
    }

    // TODO validation of user data
    public void postRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Map<String, String> messages = new HashMap<>();
        request.setAttribute("messages", messages);
        request.setAttribute("request", request);

        User user = new User();
        user.setFirstname(request.getParameter("firstname"));
        user.setLastname(request.getParameter("lastname"));
        user.setEmail(request.getParameter("email"));
        user.setBalance(150000);
        user.setPassword(request.getParameter("password"));
        user.setDate(request.getParameter("dateofbirth"));

        try {
            this.userService.createUser(user);
        } catch (ValidationException e) {
            Iterator<ConstraintViolation<Object>> it = e.getViolations().iterator();
            while (it.hasNext()) {
                ConstraintViolation violation = it.next();
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();

                String previousMessages = messages.get(field);
                if (previousMessages != null) {
                    messages.put(field, previousMessages + ". " + message);
                } else {
                    messages.put(field, message);
                }
            }
            request.getRequestDispatcher("/views/registration.jsp").forward(request, response);
            return;
        }
        this.authService.login(request.getSession(), user);
        response.sendRedirect("/");
    }

}
