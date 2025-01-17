package at.technikum.service;

import at.technikum.api.controller.DeckController;
import at.technikum.api.controller.PackageController;
import at.technikum.api.controller.SessionController;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.http.Method;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import at.technikum.httpserver.server.Service;

import java.util.Objects;
import java.util.UUID;

public class PackageService implements Service {
    private final SessionController sessionController;
    private final PackageController packageController;
    private final DeckController deckController;

    public PackageService() {
        this.sessionController = new SessionController();
        this.packageController = new PackageController();
        this.deckController = new DeckController();
    }

    @Override
    public Response handleRequest(Request request) {
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
                    /*return new Response(
                            HttpStatus.CREATED,
                            ContentType.JSON,
                            "{ \"message\" : \"CREATED\" }"
                    );*/
                if (request.getMethod() == Method.GET &&
                        request.getPathParts().size() > 1) {
                    //return this.sessionController.getWeather(request.getPathParts().get(1));
                } else if (request.getMethod() == Method.GET) {
                    //return this.sessionController.getWeatherPerRepository();
                    //return this.weatherController.getWeatherPerRepository();
                } else if (request.getMethod() == Method.POST && request.getPathParts().size() == 1
                        && request.getPathParts().get(0).equals("packages")) {
                    if (Objects.equals(token, "admin-mtcgToken")) {
                        return this.packageController.addPackage(request);
                    }
                    else {

                    }
                } else if (request.getMethod() == Method.POST && request.getPathParts().size() > 1
                        && request.getPathParts().get(1).equals("packages")) {
                    return this.deckController.addDeck(request);
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
