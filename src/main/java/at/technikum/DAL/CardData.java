package at.technikum.DAL;

import at.technikum.DAL.DAO.Card;

import java.util.ArrayList;
import java.util.List;

public class CardData {
    private List<Card> cardData;

    public CardData() {
        cardData = new ArrayList<>();
        cardData.add(new Card(0,"Card1",50));
        cardData.add(new Card(1,"Card2",50));
        cardData.add(new Card(2,"Card3",50));
    }

    // GET /weather/:id
    public Card getCard(Integer ID) {
        Card foundCard = cardData.stream()
                .filter(card -> ID == card.getId())
                .findAny()
                .orElse(null);

        return foundCard;
    }

    // GET /weather
    public List<Card> getCards() {
        return this.cardData;
    }

    // POST /weather
    public void addCard(Card card) {
        this.cardData.add(card);
    }
}
