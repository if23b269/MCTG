package at.technikum.DAL;

import at.technikum.DAL.DAO.User;
import at.technikum.httpserver.server.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionData {
    private List<Session> sessionData;

    public SessionData() {
        sessionData = new ArrayList<>();
    }
    // POST /session
    public void addSession(Session session) {
        if (this.sessionData.contains(session)) {
            throw new IllegalArgumentException("User already logged in");
        }
        this.sessionData.add(session);
    }
}
