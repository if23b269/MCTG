package at.technikum.DAL;

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
        this.sessionData.add(session);
    }
}
