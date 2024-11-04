package at.technikum.api.controller;

import at.technikum.DAL.DAO.User;
import at.technikum.DAL.UserData;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class UserController extends Controller {
    private UserData userDAL;

    public UserController() {
        this.userDAL = new UserData();
        // Nur noch für die Dummy-JUnit-Tests notwendig. Stattdessen ein RepositoryPattern verwenden.

    }

    // GET /user(:username
    public Response getUser(String username)
    {
        try {
            User userData = this.userDAL.getUser(username);
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String userDataJSON = this.getObjectMapper().writeValueAsString(userData);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    userDataJSON
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }
    // GET /weather
    public Response getUsers() {
        try {
            List<User> userData = this.userDAL.getUserData();
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String userDataJSON = this.getObjectMapper().writeValueAsString(userData);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    userDataJSON
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    // POST /weather
    public Response addUser(Request request) {
        try {

            // request.getBody() => "{ \"id\": 4, \"city\": \"Graz\", ... }
            User user = this.getObjectMapper().readValue(request.getBody(), User.class);
            //List<User> users = this.getUserDAL().getUserData();
            //users.add(user);
            /*for (int i = 0; i < users.size(); i++) {
                System.out.println(users.get(i).getUsername());
            }*/
            /*UserData userData = this.getUserDAL();
            userData.addUser(user);
            userData.setUserData(userData.getUserData());
            this.setUserDAL(userData);*/
            this.userDAL.addUser(user);


            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    "{ message: \"Success\" }"
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

    public static void handleRequest(String method, String path, BufferedWriter out) throws IOException {
        if (method.equals("GET")) {
            if (path.equals("/users")) {
                sendUserList(out);
            } else if (path.matches("/users/\\d+")) {
                sendUserDetails(out, path);
            } else {
                sendNotFound(out);
            }
        } else {
            sendMethodNotAllowed(out);
        }
    }

    private static void sendUserList(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: application/json\r\n");
        out.write("\r\n");
        out.write("[{\"id\": 1, \"name\": \"John\"}, {\"id\": 2, \"name\": \"Jane\"}]");
        out.flush();
    }

    private static void sendUserDetails(BufferedWriter out, String path) throws IOException {
        String userId = path.split("/")[2];
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: application/json\r\n");
        out.write("\r\n");
        out.write("{\"id\": " + userId + ", \"name\": \"John Doe\"}");
        out.flush();
    }

    private static void sendNotFound(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 404 Not Found\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("404 - User Not Found");
        out.flush();
    }

    private static void sendMethodNotAllowed(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 405 Method Not Allowed\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("405 - Method Not Allowed");
        out.flush();
    }

    public UserData getUserDAL() {
        return userDAL;
    }

    public void setUserDAL(UserData userDAL) {
        this.userDAL = userDAL;
    }
}
