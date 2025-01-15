package at.technikum.game;

import at.technikum.DAL.DAO.Card;
import at.technikum.DAL.DAO.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleLogic {

    // Method to simulate a battle between two users
    public static String battle(User user1, User user2) {
        List<Card> deck1 = new ArrayList<>(user1.getDeck().getSelectedCards());
        List<Card> deck2 = new ArrayList<>(user2.getDeck().getSelectedCards());

        if (deck1.isEmpty() || deck2.isEmpty()) {
            return "One or both players do not have any cards in their decks!";
        }

        Random rand = new Random();
        int roundCount = 0;

        // Limit the rounds to 100 to avoid infinite loops
        while (roundCount < 100) {
            roundCount++;

            // Pick a random card from each deck
            Card card1 = deck1.get(rand.nextInt(deck1.size()));
            Card card2 = deck2.get(rand.nextInt(deck2.size()));

            String result = battleBetweenCards(user1, card1, user2, card2);

            // If the result is not a draw, remove defeated cards from the decks and transfer them
            if (!result.contains("draw")) {
                // Remove the defeated cards from the loser’s deck and add them to the winner’s deck
                if (result.contains(user1.getName())) {
                    deck1.add(card2); // User1 wins, add card2 to user1's deck
                    deck2.remove(card2); // Remove card2 from user2's deck
                } else {
                    deck2.add(card1); // User2 wins, add card1 to user2's deck
                    deck1.remove(card1); // Remove card1 from user1's deck
                }
            }
            System.out.println(roundCount + ": " + result);

            if (deck1.isEmpty() || deck2.isEmpty()) {
                break;
            }
        }

        // Check if the game ended in a draw (both decks are empty or rounds reached 100)
        if (roundCount == 100) {
            return "Game ended in a draw after 100 rounds.";
        }

        return "Battle finished!";
    }

    // Method to calculate battle between two cards
    private static String battleBetweenCards(User user1, Card card1, User user2, Card card2) {
        double damage1 = calculateEffectiveDamage(card1, card2);
        double damage2 = calculateEffectiveDamage(card2, card1);

        /// Goblins are too afraid of Dragons to attack
        if (card1.getMonsterType() == MonsterType.GOBLIN && card2.getMonsterType() == MonsterType.DRAGON) {
            damage1 = 0; // Goblins are afraid of Dragons and won't deal any damage
        }
        if (card2.getMonsterType() == MonsterType.GOBLIN && card1.getMonsterType() == MonsterType.DRAGON) {
            damage2 = 0; // Goblins are afraid of Dragons and won't deal any damage
        }

        // Wizards can control Orks, so Orks do no damage
        if (card1.getMonsterType() == MonsterType.WIZARD && card2.getMonsterType() == MonsterType.ORK) {
            damage2 = 0; // Wizard can control Orks, so Ork does no damage
        }
        if (card2.getMonsterType() == MonsterType.WIZARD && card1.getMonsterType() == MonsterType.ORK) {
            damage1 = 0; // Wizard can control Orks, so Ork does no damage
        }

        // Knights drown instantly when attacked by WaterSpells
        if (card1.getMonsterType() == MonsterType.KNIGHT && (card2.getType() == CardType.SPELL && card2.getElement() == ElementType.WATER )) {
            damage2 = 0; // WaterSpells make Knights drown instantly
        }
        if (card2.getMonsterType() == MonsterType.KNIGHT && (card1.getType() == CardType.SPELL && card1.getElement() == ElementType.WATER )) {
            damage1 = 0; // WaterSpells make Knights drown instantly
        }

        // Kraken is immune to spells
        if (card1.getMonsterType() == MonsterType.KRAKEN && card2.getType() == CardType.SPELL) {
            damage1 = 0; // Kraken is immune to spells
        }
        if (card2.getMonsterType() == MonsterType.KRAKEN && card1.getType() == CardType.SPELL) {
            damage2 = 0; // Kraken is immune to spells
        }

        // FireElves evade Dragon attacks
        if (card1.getMonsterType() == MonsterType.FIRE_ELF && card2.getMonsterType() == MonsterType.DRAGON) {
            damage2 = 0; // FireElves evade Dragon attacks
        }
        if (card2.getMonsterType() == MonsterType.FIRE_ELF && card1.getMonsterType() == MonsterType.DRAGON) {
            damage1 = 0; // FireElves evade Dragon attacks
        }

        // Compare damage dealt by each card
        if (damage1 > damage2) {
            user1.setElo(user1.getElo() + 10); // User 1 wins, increase Elo
            user2.setElo(user2.getElo() - 10); // User 2 loses, decrease Elo
            return user1.getName() + " wins! " + card1.getName() + " dealt " + damage1 + " damage to " + card2.getName() + "!";
        } else if (damage1 < damage2) {
            user2.setElo(user2.getElo() + 10); // User 2 wins, increase Elo
            user1.setElo(user1.getElo() - 10); // User 1 loses, decrease Elo
            return user2.getName() + " wins! " + card2.getName() + " dealt " + damage2 + " damage to " + card1.getName() + "!";
        } else {
            return "It's a draw! Both players dealt the same damage.";
        }
    }

    // Method to calculate the effective damage based on element effectiveness
    private static double calculateEffectiveDamage(Card attacker, Card defender) {
        double damage = attacker.getDamage();

        switch (attacker.getElement()) {
            case WATER:
                if (defender.getElement() == ElementType.FIRE) {
                    damage *= 2; // Water -> Fire: Double damage
                }
                break;
            case FIRE:
                if (defender.getElement() == ElementType.NORMAL) {
                    damage *= 2; // Fire -> Normal: Double damage
                } else if (defender.getElement() == ElementType.WATER) {
                    damage /= 2; // Fire -> Water: Half damage
                }
                break;
            case NORMAL:
                if (defender.getElement() == ElementType.WATER) {
                    damage *= 2; // Normal -> Water: Double damage
                }
                break;
        }
        return damage;
    }
}
