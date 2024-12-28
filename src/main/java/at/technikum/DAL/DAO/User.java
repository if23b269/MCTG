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
    private int coins;
    private List<Card> cards;
    //private Deck deck;
    private List<Card> stack;
    private int elo;

    // Constructor
    /*public User(String username, String password, int coins, List<Card> cards, Deck deck, List<Card> stack, int elo) {
        this.username = username;
        this.password = password;
        this.coins = coins;
        this.cards = cards;
        this.deck = deck;
        this.stack = stack;
        this.elo = elo;
    }*/
    public User(String username, String password, int coins, List<Card> cards, List<Card> stack, int elo) {
        this.username = username;
        this.password = password;
        this.coins = coins;
        this.cards = cards;
        this.stack = stack;
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /*public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }*/

    public List<Card> getStack() {
        return stack;
    }

    public void setStack(List<Card> stack) {
        this.stack = stack;
    }

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