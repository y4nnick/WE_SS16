package at.ac.tuwien.big.we16.ue2.model;

/**
 * Created by mstrasser on 4/12/16.
 */
public class Book extends Product {
    private String author;

    public Book(Integer ID, String title, String author, String img, int year) {
        super(ID, title, img, year);
        this.author = author;
    }

    public String getTitle() { return super.getName(); }
    public String getAuthor() { return this.author; }
}
