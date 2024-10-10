package at.technikum.service;

import at.technikum.api.controller.SessionController;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.http.Method;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import at.technikum.httpserver.server.Service;

public class SessionService implements Service {
    private final SessionController sessionController;

    public SessionService() {
        this.sessionController = new SessionController();
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
            return this.sessionController.addSession(request);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
