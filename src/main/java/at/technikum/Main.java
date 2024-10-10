package at.technikum;

import at.technikum.httpserver.server.Server;
import at.technikum.httpserver.utils.Router;
import at.technikum.service.SessionService;
import at.technikum.service.UserService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(10001, configureRouter());
        try {
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

        return router;
    }
}