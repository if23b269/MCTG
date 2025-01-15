package at.technikum;

import at.technikum.DAL.DAO.Card;
import at.technikum.DAL.DAO.Deck;
import at.technikum.DAL.DAO.User;
import at.technikum.game.BattleLogic;
import at.technikum.game.CardType;
import at.technikum.game.ElementType;
import at.technikum.game.MonsterType;
import at.technikum.httpserver.server.Server;
import at.technikum.httpserver.utils.Router;
import at.technikum.persistence.dao.Dao;
import at.technikum.persistence.dao.UserDao;
import at.technikum.service.PackageService;
import at.technikum.service.SessionService;
import at.technikum.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    private static Dao<User> dao;

    public static void main(String[] args) {
        Server server = new Server(10001, configureRouter());
        try {
            dao = new UserDao();
            //UserDao.initDb();

            //dao.save(new User(1,"John Doe", "pw", "mySecret"));
            //dao.save(new User(6,"John Doe", "pw"));
            //dao.save(new User(7,"admin", "istrator"));

            User user01 = getUser(6);
            //System.out.println(user01);

            //dao.update(user1, new String[]{"1","Max Musterfrau", "pw", "mySecret"});
            //System.out.println();

            User user02 = getUser(2);
            dao.delete(user02);
            //dao.save(new User(2,"Jane Doe", "pw", "mySecret"));
            dao.saveWithId(new User(2,"Jane Doe", "pw"));

            //dao.getAll().forEach(System.out::println);

            Card[] cards = new Card[]{
                    new Card("001", "Flame Dragon", 50.5, CardType.MONSTER, ElementType.FIRE, MonsterType.DRAGON),
                    new Card("002", "Thunder Beast", 45.0, CardType.SPELL, ElementType.WATER, MonsterType.ORK),
                    new Card("003", "Ocean Serpent", 30.0, CardType.MONSTER, ElementType.WATER, MonsterType.KRAKEN),
                    new Card("004", "Earth Golem", 40.0, CardType.SPELL, ElementType.NORMAL, MonsterType.KRAKEN),
                    new Card("005", "Wind Griffin", 35.0, CardType.MONSTER, ElementType.WATER, MonsterType.KRAKEN),
                    new Card("006", "Dark Phoenix", 60.0, CardType.SPELL, ElementType.NORMAL, MonsterType.KRAKEN),
                    new Card("007", "Light Angel", 25.5, CardType.MONSTER, ElementType.WATER, MonsterType.KRAKEN),
                    new Card("008", "Stone Giant", 70.0, CardType.SPELL, ElementType.NORMAL, MonsterType.KRAKEN)
            };
            Deck deck1 = new Deck(List.of(cards[0], cards[1], cards[2], cards[3]));
            Deck deck2 = new Deck(List.of(cards[4], cards[5], cards[6], cards[7]));

            User user1 = new User(1L, "dragonMaster", "dragon123", "John Doe", 1500, deck1, 1200, "A master of dragons", "image1.png");
            User user2 = new User(2L, "beastKing", "beast456", "Jane Smith", 1200, deck2, 1100, "King of beasts", "image2.png");

            System.out.println(BattleLogic.battle(user1, user2));

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Router configureRouter()
    {
        Router router = new Router();
        router.addService("/users", new UserService());
        router.addService("/sessions", new SessionService());
        router.addService("/packages", new PackageService());
        router.addService("/transactions", new PackageService());

        return router;
    }

    private static User getUser(int id) {
        Optional<User> user = dao.get(id);

        return user.orElseGet(
                User::new
        );
    }
}