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