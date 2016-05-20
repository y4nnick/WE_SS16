package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.User;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Created by mstrasser on 5/20/16.
 */
public class BIGBoardService {
    // The URL where the BIG Board Web Service is located.
    private static final Logger logger = LogManager.getLogger("BIGBoardService");
    private String url;
    private Charset charset = StandardCharsets.UTF_8;
    private Gson gson = new Gson();

    /**
     * Constructor for the BIG Board Service.
     * @param url The URL where the BIG Board Web Service is located.
     */
    public BIGBoardService(String url) {
        this.url = url;
    }

    /**
     * Post a sale to the BIG Board.
     * @return The UUID of the BIG Board entry.
     * TODO: Remove debug flag & check logging
     */
    public String postToBoard(User u, Product p, boolean debug) {
        try {
            StringBuilder response = new StringBuilder();
            HttpURLConnection connection = this.buildConnection();
            JSONRequestAttributes attributes = new JSONRequestAttributes();
            attributes.date = Instant.now().toString();

            if(debug) {
                attributes.name = "Test Webservice";
                attributes.product = "Testproduct";
                attributes.price = "23.00";
            } else {
                attributes.name = u.getFullName();
                attributes.product = p.getName();
                attributes.price = String.valueOf(p.getHighestBid().getConvertedAmount());
            }

            String jsonRequest = gson.toJson(attributes);

            connection.setRequestProperty("Content-Length", String.valueOf(jsonRequest.length()));
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonRequest);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for( String line; (line = reader.readLine()) != null; ) {
                response.append(line);
            }

            JSONRequestAttributes jsonResponse = gson.fromJson(response.toString(), JSONRequestAttributes.class);
            writer.close();
            reader.close();

            return jsonResponse.id;
        } catch (IOException e) {
            BIGBoardService.logger.error("Error connecting to server or malformed content.");
        }

        return null;
    }

    /**
     * Builds a URL connection.
     * @return The configured URL connection.
     * @throws IOException If either the URL is malformed, the request protocol is invalid or opening the connection
     * fails.
     */
    private HttpURLConnection buildConnection() throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json");

        return connection;
    }

    class JSONRequestAttributes {
        public String name;
        public String product;
        public String price;
        public String date;
        public String id;
    }
}
