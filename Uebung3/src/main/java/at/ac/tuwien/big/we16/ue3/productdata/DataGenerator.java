package at.ac.tuwien.big.we16.ue3.productdata;

import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.User;
import at.ac.tuwien.big.we16.ue3.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DataGenerator {

    private static final String PERSISTENCE_UNIT_NAME = "defaultPersistenceUnit";
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    private  EntityManager em;

    public void generateData() {

        em = factory.createEntityManager();
        em.getTransaction().begin();

        generateUserData();
        generateProductData();
        insertRelatedProducts();

        em.getTransaction().commit();
        em.close();
    }

    private void generateUserData() {

        try{

            DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
            User bidBot = new User("Bid","Bot", "bot@bigbid.com", "secret1", Integer.MAX_VALUE,formatter.parse("1.1.1990"));
            User userEdith =    new User("Edith","Sarau",   "edith@bigbid.at",   "secret2", 150000,formatter.parse("1.1.1993"));
            User userMichael =  new User("Michael","Strasser", "michael@bigbid.at", "secret3",150000,formatter.parse("2.1.1993"));
            User userYannick =  new User("Yannick","Schwarenthorer", "yannick@bigbid.at", "secret4",150000,formatter.parse("3.1.1993"));

            em.persist(bidBot);
            em.persist(userEdith);
            em.persist(userMichael);
            em.persist(userYannick);
        }catch (ParseException e){
           System.err.print(e);
        }

    }

    private void generateProductData() {

        for(JSONDataLoader.Book book : JSONDataLoader.getBooks()){

            Product p = new Product();
            p.setName(book.getTitle());
            p.setImage(book.getImg());
            p.setImageAlt("Bookcover of " + p.getName());
            p.setYear(Integer.valueOf(book.getYear()));
            p.setProducer(book.getAuthor());
            p.setAuctionEnd(getRandomDateInFuture());
            p.setExpired(false);

            em.persist(p);
        }

        for(JSONDataLoader.Movie movie : JSONDataLoader.getFilms()){

            Product p = new Product();
            p.setName(movie.getTitle());
            p.setImage(movie.getImg());
            p.setImageAlt("Bookcover of " + p.getName());
            p.setYear(Integer.valueOf(movie.getYear()));
            p.setProducer(movie.getDirector());
            p.setAuctionEnd(getRandomDateInFuture());
            p.setExpired(false);

            em.persist(p);
        }

        for(JSONDataLoader.Music music : JSONDataLoader.getMusic()){

            Product p = new Product();
            p.setName(music.getAlbum_name());
            p.setImage(music.getImg());
            p.setImageAlt("Bookcover of " + p.getName());
            p.setYear(Integer.valueOf(music.getYear()));
            p.setProducer(music.getArtist());
            p.setAuctionEnd(getRandomDateInFuture());
            p.setExpired(false);

            em.persist(p);
        }
    }

    /**
     * Returns a Date which is between 1 and 9 Minutes in the future
     * @return a Date which is between 1 and 9 Minutes in the future
     */
    private static Date getRandomDateInFuture(){

        //Generate Number between 1 and 9
        Random ran = new Random();
        int minutesOffset = ran.nextInt(9) + 1;

        //Add minutes to current Time
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE,minutesOffset);

        return cal.getTime();
    }


    private void insertRelatedProducts() {
        // TODO load related products from dbpedia and write them to the database
    }
}
