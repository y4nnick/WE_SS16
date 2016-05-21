package at.ac.tuwien.big.we16.ue3.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String salutation;

    @NotNull(message = "First name is compulsory")
    private String firstname;

    @NotNull(message = "Last name is compulsory")
    private String lastname;

    @NotNull(message = "E-Mail Address is compulsory")
    @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", message = "E-Mail format ist not valid 'abc@def.com'")
    private String email;

    @NotNull
    @Size(min = 4, max = 8, message = "the password must be between 4 and 8 characters long")
    private String password;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateOfBirth")
    @Past(message = "Date of Birth must be in the past")
    private Date date;

    private int balance;
    private int runningAuctionsCount;
    private int wonAuctionsCount;
    private int lostAuctionsCount;

    public User(){
    }

    public User(String firstname, String lastname, String email, String password, int balance, Date date) {
        setFirstname(firstname);
        setLastname(lastname);
        this.balance = balance;
        this.email = email;
        this.password = password;
        this.date = date;
    }
    public boolean overEighteen(){
        if (date == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        Date beforeDate = new Date(now.getDayOfMonth(), now.getMonthValue(), now.getYear() - 18);

        return date.before(beforeDate);
    }

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public float getConvertedBalance() {
        float convertedBalance = (float)this.balance;
        return convertedBalance / 100;
    }

    public void decreaseBalance(int amount) {
        this.balance -= amount;
    }

    public void increaseBalance(int amount) {
        this.balance += amount;
    }

    public int getRunningAuctionsCount() {
        return this.runningAuctionsCount;
    }

    public void incrementRunningAuctions() {
        this.runningAuctionsCount++;
    }

    public void decrementRunningAuctions() {
        this.runningAuctionsCount--;
    }

    public int getWonAuctionsCount() {
        return this.wonAuctionsCount;
    }

    public int getLostAuctionsCount() {
        return this.lostAuctionsCount;
    }

    public void incrementLostAuctionsCount() {
        this.lostAuctionsCount++;
    }

    public void incrementWonAuctionsCount() {
        this.wonAuctionsCount++;
    }

    public boolean hasSufficientBalance(int amount) {
        return this.balance >= amount;
    }

    public String getEmail() {
        return email;
    }

    public String getSalutation() {
        return salutation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname.isEmpty() ? null : firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname.isEmpty() ? null : lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance(){
        return this.balance;
    }

    public void setRunningAuctionsCount(int runningAuctionsCount) {
        this.runningAuctionsCount = runningAuctionsCount;
    }

    public void setWonAuctionsCount(int wonAuctionsCount) {
        this.wonAuctionsCount = wonAuctionsCount;
    }

    public void setLostAuctionsCount(int lostAuctionsCount) {
        this.lostAuctionsCount = lostAuctionsCount;
    }
}
