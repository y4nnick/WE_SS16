package at.ac.tuwien.big.we16.ue3.productdata;

import at.ac.tuwien.big.we.dbpedia.api.DBPediaService;
import at.ac.tuwien.big.we.dbpedia.api.SelectQueryBuilder;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPedia;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPediaOWL;
import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.ProductType;
import at.ac.tuwien.big.we16.ue3.model.RelatedProduct;
import at.ac.tuwien.big.we16.ue3.model.User;
import at.ac.tuwien.big.we16.ue3.service.ProductService;
import at.ac.tuwien.big.we16.ue3.service.UserService;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataGenerator {

    private static final String PERSISTENCE_UNIT_NAME = "defaultPersistenceUnit";
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    private  EntityManager em;

    public void generateData() {

        em = factory.createEntityManager();
        em.getTransaction().begin();
        generateUserData();
        generateProductData();
        em.getTransaction().commit();

        em.getTransaction().begin();
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
            p.setType(ProductType.BOOK);

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
            p.setType(ProductType.FILM);

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
            p.setType(ProductType.ALBUM);

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

        // TODO log if not available

        // Check if DBpedia is available
        if(!DBPediaService.isAvailable()) {
            return;
        }

        ProductService service = new ProductService();
        Collection<Product> products = service.getAllProducts();

        for (Product p : products){

            Resource producer = DBPediaService.loadStatements(DBPedia.createResource(p.getProducer().replace(" ","_")));

            Resource own = DBPediaService.loadStatements(DBPedia.createResource(p.getName().replace(" ","_")));

            // Build SPARQL-query
            SelectQueryBuilder query = DBPediaService.createQueryBuilder()
                    .setLimit(5)
                    .addPredicateExistsClause(FOAF.name)
                    .addFilterClause(RDFS.label, Locale.GERMAN)
                    .addFilterClause(RDFS.label, Locale.ENGLISH)
                    .addMinusClause(DBPediaOWL.name, own);

            if(p.getType().equals(ProductType.FILM)){
                query.addWhereClause(RDF.type, DBPediaOWL.Film);
                query.addWhereClause(DBPediaOWL.director, producer);
            }else if(p.getType().equals(ProductType.ALBUM)){
                query.addWhereClause(RDF.type, DBPediaOWL.Album);
                query.addWhereClause(DBPediaOWL.band,producer);
            }else if(p.getType().equals(ProductType.BOOK)){
                query.addWhereClause(RDF.type, DBPediaOWL.Book);
                query.addWhereClause(DBPediaOWL.author, producer);
            }

            Model related = DBPediaService.loadStatements(query.toQueryString());
            List<String> names = DBPediaService.getResourceNames(related, Locale.GERMAN);

            for(String name : names){
                RelatedProduct relatedProduct = new RelatedProduct();
                relatedProduct.setProduct(p);
                relatedProduct.setName(name);
                em.persist(relatedProduct);
            }
        }
    }
}
