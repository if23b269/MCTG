package at.technikum.DAL.DAO;

import at.technikum.game.CardType;
import at.technikum.game.MonsterType;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import at.technikum.game.ElementType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Card {
    @JsonAlias({"Id"})
    private String id;
    @JsonAlias({"Name"})
    private String name;
    @JsonAlias({"Damage"})
    private Double damage;
    private CardType type;
    private ElementType element;
    private MonsterType monsterType;

    // Constructor
    public Card(String id, String name, Double damage) {
        this.id = id;
        this.name = name;
        this.damage = damage;
    }

    public Card(String fireMonster, CardType cardType, ElementType elementType, double damage) {
        this.id = fireMonster;
        this.type = cardType;
        this.element = elementType;
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
