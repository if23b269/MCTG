package at.technikum.httpserver.server;

import at.technikum.DAL.DAO.User;

public class Session {
    private int id;
    private User user;
    private String token;

    public Session(int id, User user, String token) {
        this.id = id;
        this.user = user;
        this.token = token;
    }

    public Session(User user, String token) {
        this.id = id;
        this.user = user;
        this.token = token;
    }

    public Session() {
    }

    public User getUser() {
        return user;
    }
}
