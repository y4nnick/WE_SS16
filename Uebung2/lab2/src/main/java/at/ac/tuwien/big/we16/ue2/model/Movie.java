package at.ac.tuwien.big.we16.ue2.model;

/**
 * Created by mstrasser on 4/12/16.
 */
public class Movie extends Product {
    private String director;

    public Movie(String ID, String title, String director, String img, int year) {
        super(ID, title, img, year);
        this.director = director;
    }

    public String getTitle() { return super.getName(); }
    public String getDirector() { return this.director; }
}
