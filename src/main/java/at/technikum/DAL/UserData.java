package at.technikum.DAL;

import at.technikum.DAL.DAO.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserData {
    private List<User> userData;

    public UserData() {
        userData = new ArrayList<>();
        userData.add(new User("User1","pwd1", 5));
        userData.add(new User("User2","pwd2", 5));
        userData.add(new User("User3","pwd3", 5));
    }

    // GET /weather/:id
    public User getUser(String username) {
        User foundUser = userData.stream()
                .filter(user -> Objects.equals(username, user.getUsername()))
                .findAny()
                .orElse(null);

        return foundUser;
    }

    // GET /weather
    public List<User> getUsers() {
        return this.userData;
    }

    // POST /weather
    public void addUser(User user) {
        if(userData.contains(user)) {
            throw new IllegalArgumentException("User already exists");
        }
        this.userData.add(user);
    }
}
