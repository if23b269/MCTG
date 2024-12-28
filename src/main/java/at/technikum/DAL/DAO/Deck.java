package at.technikum.DAL.DAO;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Deck {
    private int id;
    private String name;
    private List<Card> selectedCards;
    //+selectCard(card: Card)
    //+removeCard(card: Card)
}
