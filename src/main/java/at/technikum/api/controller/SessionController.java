package at.technikum.api.controller;

import at.technikum.DAL.DAO.User;
import at.technikum.DAL.SessionHandler;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import at.technikum.httpserver.server.Session;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class SessionController extends Controller {
    @Getter
    private SessionHandler sessionsessionHandler;
    private final String SECRET_KEY = "B4EFA2836BDED9533BA6E7AD51C4A";

    public SessionController() {
        this.sessionsessionHandler = new SessionHandler();
        // Nur noch für die Dummy-JUnit-Tests notwendig. Stattdessen ein RepositoryPattern verwenden.

    }

    // POST /weather
    public Response addSession(Request request, List<User> users) {
        try {

            // request.getBody() => "{ \"id\": 4, \"city\": \"Graz\", ... }
            User user = this.getObjectMapper().readValue(request.getBody(), User.class);

            if (!users.contains(user)) {
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        "{ \"message\" : \"User not found\"}"
                );
            }
            //JSON Web Token
            //String token = generateJWT(user.getUsername());
            String token = "mtcgToken";
            Session session = new Session(user, user.getUsername()+ "-" + token);
            System.out.println(user);
            this.sessionsessionHandler.addSession(session);


            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{ message: \"Success - with generated token for the user, here: " + user.getUsername() + "-" + token + "\" }"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            return new Response(
                    HttpStatus.CONFLICT,
                    ContentType.JSON,
                    "{ message: \""+ e.getMessage() + "\" }"
            );
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }

    private String toJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }
            first = false;
        }
        json.append("}");
        return json.toString();
    }

    public static void handleRequest(String method, String path, BufferedWriter out) throws IOException {
        if (method.equals("GET") && path.equals("/sessions")) {
            sendSessionList(out);
        } else {
            sendNotFound(out);
        }
    }

    private static void sendSessionList(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: application/json\r\n");
        out.write("\r\n");
        out.write("[{\"sessionId\": \"abc123\", \"userId\": 1}, {\"sessionId\": \"xyz789\", \"userId\": 2}]");
        out.flush();
    }

    private static void sendNotFound(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 404 Not Found\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("404 - Session Not Found");
        out.flush();
    }
}
