package at.technikum.DAL.DAO;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@Setter
@Getter
public class Package {
    private String id;
    private List<Card> cards;
    private int price = 5;
}