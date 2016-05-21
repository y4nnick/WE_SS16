package at.ac.tuwien.big.we16.ue3.model;

import at.ac.tuwien.big.we16.ue3.exception.InvalidBidException;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Product {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;
    private String image;
    private String imageAlt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date auctionEnd;

    private ProductType type;

    private int year;
    private String producer;
    private boolean expired;

    @OneToMany(mappedBy = "product")
    private List<RelatedProduct> relatedProducts;

    @OneToMany(mappedBy = "product")
    private List<Bid> bids;

    public Bid getHighestBid() {
        Bid highest = null;
        int highestAmount = 0;
        for (Bid bid : this.bids) {
            if (bid.getAmount() > highestAmount) {
                highest = bid;
            }
        }
        return highest;
    }

    public boolean hasAuctionEnded() {
        return this.getAuctionEnd().before(new Date());
    }

    public void addBid(Bid bid) throws InvalidBidException {
        this.bids.add(bid);
    }

    public boolean hasExpired() {
        return expired;
    }

    public void setExpired() {
        this.expired = true;
    }

    public Set<User> getUsers() {
        Set<User> users = this.bids.stream().map(Bid::getUser).collect(Collectors.toSet());
        return users;
    }

    public boolean hasBids() {
        return this.bids.size() > 0;
    }

    public boolean isValidBidAmount(int amount) {
        return !this.hasBids() || this.getHighestBid().getAmount() < amount;
    }

    public boolean hasBidByUser(User user) {
        for (Bid bid : this.bids) {
            if (bid.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public List<RelatedProduct> getRelatedProducts() {
        return relatedProducts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageAlt(String imageAlt) {
        this.imageAlt = imageAlt;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public void setRelatedProducts(List<RelatedProduct> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
}
