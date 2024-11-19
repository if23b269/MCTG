package at.technikum;

import at.technikum.httpserver.server.Server;
import at.technikum.httpserver.utils.Router;
import at.technikum.persistence.UserRepository;
import at.technikum.service.SessionService;
import at.technikum.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.ConnectException;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        try {
            // Perform a database operation
            ConfigurableApplicationContext springContext = SpringApplication.run(SpringBootConsoleApplication.class, args);
        } catch (Exception e) {
            // Handle JDBCConnectionException
            // Log and handle the connection error
            System.err.println("Database connection error: " + e.getMessage());
        }

        log.info("APPLICATION FINISHED");
    }

    public static Router configureRouter()
    {
        Router router = new Router();
        UserService userService = new UserService();
        router.addService("/users", userService);
        router.addService("/sessions", new SessionService(userService));

        return router;
    }
}