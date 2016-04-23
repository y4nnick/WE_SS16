package at.ac.tuwien.big.we16.ue2.productdata;

import at.ac.tuwien.big.we16.ue2.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JSONDataLoader {

    //Stores all products of the system, the key-value is the id from the product
    private static ConcurrentHashMap<Integer, Product> productHashMap = new ConcurrentHashMap<>();

    private static Product_Loader products;

    public static ConcurrentHashMap<Integer, Product> getProducts(){
        if (products == null)loadProducts();
        return productHashMap;
    }

    public static Product getById(Integer id){
        if (products == null)loadProducts();
        return productHashMap.get(id);
    }

    private static void loadProducts() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("products.json");
        Reader reader = new InputStreamReader(is);
        Gson gson = new GsonBuilder().create();
        products = gson.fromJson(reader, Product_Loader.class);

        int id = 1;
        for(JSONDataLoader.Music_Loader m: JSONDataLoader.getMusic()) {
            productHashMap.put(id,new Music(id,m.getAlbum_name(),m.getArtist(),m.getImg(),Integer.valueOf(m.getYear())));
            id++;
        }

        for(JSONDataLoader.Book_Loader m: JSONDataLoader.getBooks()) {
            productHashMap.put(id,new Book(id, m.getTitle(),m.getAuthor(), m.getImg(), Integer.valueOf(m.getYear())));
            id++;
        }

        for(JSONDataLoader.Movie_Loader m: JSONDataLoader.getFilms()) {
            productHashMap.put(id,new Movie(id, m.getTitle(),m.getDirector(), m.getImg(), Integer.valueOf(m.getYear())));
            id++;
        }

        //Set auction end dates
        for(Product p: productHashMap.values()){
            p.setAuctionEnd(getRandomDateInFuture());
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

    private static Music_Loader[] getMusic() {
        return products.getMusic();
    }

    private static Movie_Loader[] getFilms() {
        return products.getMovies();
    }

    private static Book_Loader[] getBooks() {
        return products.getBooks();
    }

    public class Product_Loader {
        private Music_Loader[] music;
        private Book_Loader[] books;
        private Movie_Loader[] movies;

        public Music_Loader[] getMusic() {
            return music;
        }

        public void setMusic(Music_Loader[] music) {
            this.music = music;
        }

        public Book_Loader[] getBooks() {
            return books;
        }

        public void setBooks(Book_Loader[] books) {
            this.books = books;
        }

        public Movie_Loader[] getMovies() {
            return movies;
        }

        public void setMovies(Movie_Loader[] movies) {
            this.movies = movies;
        }
    }

    public class Music_Loader {

        private int id;
        private String album_name;
        private String artist;
        private String year;
        private String img;

        public String getAlbum_name() {
            return album_name;
        }

        public void setAlbum_name(String album_name) {
            this.album_name = album_name;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public class Book_Loader {

        public int id;
        private String title;
        private String author;
        private String year;
        private String img;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public class Movie_Loader {

        public int id;
        private String title;
        private String director;
        private String year;
        private String img;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}
