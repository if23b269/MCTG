package at.technikum.DAL.DAO;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
public class Card {
    @JsonAlias({"Id"})
    private String id;
    @JsonAlias({"Name"})
    private String name;
    @JsonAlias({"Damage"})
    private Double damage;

    // Constructor
    public Card(String id, String name, Double damage) {
        this.id = id;
        this.name = name;
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
