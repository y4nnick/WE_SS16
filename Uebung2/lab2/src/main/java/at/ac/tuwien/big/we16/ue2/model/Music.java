package at.ac.tuwien.big.we16.ue2.model;

/**
 * Created by mstrasser on 4/12/16.
 */
public class Music extends Product {
    private String artist;

    public Music(String ID, String title, String artist, String img, int year) {
        super(ID, title, img, year);
        this.artist = artist;
    }

    public String getTitle() { return super.getName(); }
    public String getArtist() { return this.artist; }
}
