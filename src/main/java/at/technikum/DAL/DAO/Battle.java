package at.technikum.DAL.DAO;

import lombok.*;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@Setter
@Getter
public class Battle {
    private User player1;
    private User player2;
    private int roundLimit = 100;

    private void battle() {

    }

    private int roundOutcome(Card card1, Card card2) {
        return 0;
    }
}
