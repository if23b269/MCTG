package at.technikum.DAL.DAO;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;


import java.util.List;
import java.util.Objects;

@Data
@Setter
@Getter
public class User {

    private Long id;
    @JsonAlias({"Username"})
    private String username;
    @JsonAlias({"Password"})
    private String password;
    @JsonAlias({"Name"})
    private String name;
    private int coins;
    private List<Package> packages;
    private Deck deck;
    //private List<Card> stack;
    private int elo;
    @JsonAlias({"Bio"})
    private String bio;
    @JsonAlias({"Image"})
    private String image;

    // Constructor

    public User(Long id, String username, String password, String name, int coins, Deck deck, int elo, String bio, String image) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.coins = coins;
        this.deck = deck;
        this.elo = elo;
        this.bio = bio;
        this.image = image;
    }

    /*public User(String username, String password, int coins, List<Card> cards, Deck deck, List<Card> stack, int elo) {
            this.username = username;
            this.password = password;
            this.coins = coins;
            this.cards = cards;
            this.deck = deck;
            this.stack = stack;
            this.elo = elo;
        }*/
    public User(String username, String password, int coins, List<Package> packages, Deck deck, int elo) {
        this.username = username;
        this.password = password;
        this.coins = coins;
        this.packages = packages;
        this.deck = deck;
        this.elo = elo;
    }
    public User(String username, String password, int coins) {
        this.username = username;
        this.password = password;
        this.coins = coins;
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // Jackson needs the default constructor
    public User() {
    }

    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    //UserDao!!
    public User(int anInt, String string, String string1, String string2) {
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    /*public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }*/

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User other)){
            return false;
        }
        return Objects.equals(other.username, this.username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", coins=" + coins +
                ", elo=" + elo +
                '}';
    }
}