package at.ac.tuwien.big.we16.ue2.service;

import at.ac.tuwien.big.we16.ue2.model.BidBot;
import at.ac.tuwien.big.we16.ue2.model.Product;
import at.ac.tuwien.big.we16.ue2.model.User;
import at.ac.tuwien.big.we16.ue2.productdata.JSONDataLoader;
import at.ac.tuwien.big.we16.ue2.productdata.UserHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by mstrasser on 4/22/16.
 *
 * Instantiates global variables and the like.
 */
public final class InitListener implements ServletContextListener {

    // @TODO: In NotifierService this is final, not possible here - is that a problem?
    private ScheduledExecutorService executor;

    @Override
    /**
     * When called and products are not loaded, all products will be loaded with JSONDataLoader and added as an
     * application-wide variable. Then the BidBot will be started with the loaded products. Also the user get generated.
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext application = servletContextEvent.getServletContext();

        Object obj = application.getAttribute("products");
        if (obj == null || !(obj instanceof ConcurrentHashMap)) {
            this.executor = Executors.newSingleThreadScheduledExecutor();
            ConcurrentHashMap<Integer, Product> products = JSONDataLoader.getProducts();
            application.setAttribute("products", products);

            // Generate users
            UserHandler.generateUser();
            // Start BidBot every 10 seconds
            this.executor.scheduleAtFixedRate(new BidBot(products), 0, 10, TimeUnit.SECONDS);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServiceFactory.getNotifierService().stop();
        this.executor.shutdown();
    }
}
