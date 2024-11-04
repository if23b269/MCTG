package at.technikum.service;

import at.technikum.api.controller.UserController;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.http.Method;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import at.technikum.httpserver.server.Service;

public class UserService implements Service {
        private final UserController userController;

    public UserService() {
        this.userController = new UserController();
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET &&
                request.getPathParts().size() > 1) {
            return this.userController.getUser(request.getPathParts().get(1));
        } else if (request.getMethod() == Method.GET) {
            return this.userController.getUsers();
            //return this.weatherController.getWeatherPerRepository();
        } else if (request.getMethod() == Method.POST) {
            return this.userController.addUser(request);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    public UserController getUserController() {
        return userController;
    }
}
