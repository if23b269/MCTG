package at.technikum.DAL.DAO;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Setter
@Getter
public class Deck {
    private Long id;
    private String name;
    private List<Card> selectedCards;

    public Deck() {
        selectedCards = new ArrayList<>(4);
    }

    public Deck(String name) {
        this.name = name;
    }

    public Deck(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public Deck(List<Card> selectedCards) {
        this.selectedCards = selectedCards;
    }

    private boolean selectCard(Card card) {
        if (!selectedCards.contains(card)) {
            if (selectedCards.size() <= 4) {
                return this.selectedCards.add(card);
            }
        }
        return false;
    }

    private boolean removeCard(Card card) {
        return this.selectedCards.remove(card);
    }
}
