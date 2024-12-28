package at.technikum.DAL.DAO;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @JsonAlias({"id"})
    private Long id;
    @JsonAlias({"name"})
    private String name;
    @JsonAlias({"damage"})
    private int damage;

    // Constructor
    public Card(int id, String name, int damage) {
        this.id = Long.valueOf(id);
        this.name = name;
        this.damage = damage;
    }
    public Long getId() { return id; }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for damage
    public int getDamage() {
        return damage;
    }

    // Setter for damage
    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", damage=" + damage +
                '}';
    }
}
