package at.technikum.service;

import at.technikum.api.controller.SessionController;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.http.Method;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import at.technikum.httpserver.server.Service;

import java.util.UUID;

public class PackageService implements Service {
    private final SessionController sessionController;
    private final UserService userService;

    public PackageService(UserService userService) {
        this.sessionController = new SessionController();
        this.userService = userService;
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET &&
                request.getPathParts().size() > 1) {
            //return this.sessionController.getWeather(request.getPathParts().get(1));
        } else if (request.getMethod() == Method.GET) {
            //return this.sessionController.getWeatherPerRepository();
            //return this.weatherController.getWeatherPerRepository();
        } else if (request.getMethod() == Method.POST) {
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
                    return new Response(
                            HttpStatus.CREATED,
                            ContentType.JSON,
                            "{ \"message\" : \"CREATED\" }"
                    );
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
