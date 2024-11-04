package at.technikum.httpserver.server;

import at.technikum.DAL.DAO.User;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Session other)){
            return false;
        }
        return Objects.equals(other.token, this.token);
    }
}
