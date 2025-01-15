package at.technikum.service;

import at.technikum.api.controller.SessionController;
import at.technikum.api.controller.UserController;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.http.Method;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import at.technikum.httpserver.server.Service;

public class UserService implements Service {
        private final UserController userController;
        private final SessionController sessionController;

    public UserService() {
        this.userController = new UserController();
        this.sessionController = new SessionController();
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST) {
            return this.userController.addUser(request);
        } else {
            String token = this.sessionController.getSessionsessionHandler().tokenFromHttpHeader(
                    request.getHeaderMap().getHeader(("Authorization")));
            switch (this.sessionController.getSessionsessionHandler().verifyUUID(token, false)) {
                case -1, -2 -> {
                    return new Response(
                            HttpStatus.UNAUTHORIZED,
                            ContentType.JSON,
                            "{ \"message\" : \"UNAUTHORIZED\" }"
                    );
                }
                case -3 -> {
                    return new Response(
                            HttpStatus.FORBIDDEN,
                            ContentType.JSON,
                            "{ \"message\" : \"FORBIDDEN\" }"
                    );
                }
                case 0 -> {
                    if (request.getMethod() == Method.GET &&
                            request.getPathParts().size() > 1) {
                        return this.userController.getUser(request.getPathParts().get(1));
                    } else if (request.getMethod() == Method.GET) {
                        return this.userController.getUsers();
                        //return this.weatherController.getWeatherPerRepository();
                    } else if (request.getMethod() == Method.DELETE &&
                                request.getPathParts().size() > 1) {
                            return this.userController.deleteUser(request.getPathParts().get(1));
                    }  else if (request.getMethod() == Method.PUT) {
                        return this.userController.updateUser(request, this.sessionController.getSessionsessionHandler().
                                getSessionByToken(token).getUser());
                    }
                }
            }
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
