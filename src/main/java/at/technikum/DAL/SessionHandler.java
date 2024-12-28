package at.technikum.DAL;

import at.technikum.DAL.DAO.User;
import at.technikum.httpserver.server.Session;
import at.technikum.persistence.dao.Dao;
import at.technikum.persistence.dao.SessionDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class SessionHandler {
    private List<Session> sessions;
    private static Dao<Session> sessionDao;

    public SessionHandler() {
        sessions = new ArrayList<>();
        sessionDao = new SessionDao();
    }
    // POST /session
    public void addSession(Session session) {
        if (this.sessions.contains(session)) {
            throw new IllegalArgumentException("User already logged in");
        }
        this.sessions.add(session);
        sessionDao.save(session);
    }

    public String tokenFromHttpHeader(String headerValue) {
        try {
            return headerValue == null ? null : headerValue.replaceFirst("^Bearer ", "");
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }
    /*public int verifyUser(UUID uuid, String username) {
        return verifyUUID(uuid, username, false);
    }*/

    public int verifyUUID(String uuid, boolean requireAdmin) {
        if (uuid == null) return -1; //MISSING
        if (!containsKey(uuid)) return -2; //INVALID
        if (getSessionByToken(uuid).isAdmin() || !requireAdmin) return 0; //VALID
        return -3; //FORBIDDEN
    }

    public boolean containsKey (String uuid) {
        Collection<Session> sessions = sessionDao.getAll();
        for (Session session : sessions) {
            if (session.getToken().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public Session getSessionByToken(String uuid){
        Collection<Session> sessions = sessionDao.getAll();
        for (Session session : sessions) {
            if (session.getToken().equals(uuid)) {
                return session;
            }
        }
        return null;
    }
}
