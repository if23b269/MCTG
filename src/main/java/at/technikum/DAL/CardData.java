package at.technikum.DAL;

import at.technikum.DAL.DAO.Card;

import java.util.ArrayList;
import java.util.List;

public class CardData {
    private List<Card> cardData;

    public CardData() {
        cardData = new ArrayList<>();
        cardData.add(new Card("0","Card1",50.0));
        cardData.add(new Card("1","Card2",50.0));
        cardData.add(new Card("2","Card3",50.0));
    }

    // GET /weather/:id
    public Card getCard(String ID) {
        Card foundCard = cardData.stream()
                .filter(card -> ID.equals(card.getId()))
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
