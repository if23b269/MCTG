package at.technikum.httpserver.server;


import at.technikum.DAL.DAO.User;
import at.technikum.httpserver.utils.RequestHandler;
import at.technikum.httpserver.utils.Router;
import at.technikum.persistence.UserDBService;
import at.technikum.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{
    private int port;
    private Router router;

    public Server(int port, Router router) {
        this.port = port;
        this.router = router;
    }

    public void start() throws IOException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        System.out.println("Start http-server...");
        System.out.println("http-server running at: http://localhost:" + this.port);

        try(ServerSocket serverSocket = new ServerSocket(this.port)) {
            while(true) {
                final Socket clientConnection = serverSocket.accept();
                final RequestHandler socketHandler = new RequestHandler(clientConnection, this.router);
                executorService.submit(socketHandler);
            }
        }
    }
}
