package at.technikum.httpserver.server;

import at.technikum.DAL.DAO.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class Session {

    private Long id;
    @Getter
    private User user;
    private String token;
    private boolean admin;

    public Session(Long id, User user, String token) {
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Session other)){
            return false;
        }
        return Objects.equals(other.token, this.token);
    }
}
