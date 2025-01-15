package at.technikum.api.controller;

import at.technikum.DAL.DAO.Card;
import at.technikum.DAL.DAO.Package;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import at.technikum.persistence.dao.PackageDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Getter
public class PackageController extends Controller {
    private final PackageDao packageDao;

    public PackageController() {
        this.packageDao = new PackageDao();
        // Nur noch für die Dummy-JUnit-Tests notwendig. Stattdessen ein RepositoryPattern verwenden.

    }

    // GET /card(:username
    public Response getPackage(String uuid)
    {
        try {
            Package packageData = this.packageDao.getByUsername(uuid).get();
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String userDataJSON = this.getObjectMapper().writeValueAsString(packageData);

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
        }catch (NoSuchElementException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal DB Error\" }"
            );
        }
    }

    public Response getPackages() {
        try {
            List<Package> packageData = this.packageDao.getAll();
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String userDataJSON = this.getObjectMapper().writeValueAsString(packageData);

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

    // POST /package
    public Response addPackage(Request request) {
        try {
            List<Card> cards = parseCardList(request.getBody());

            if (cards.size() != 5) {
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        "{ \"message\": \""+ "a pack must consist of 5 cards" + "\" }"
                );
            }

            Package apackage = new Package(UUID.randomUUID().toString(),cards,5.0);
            this.packageDao.save(apackage);


            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    "{ \"message\": \"Success\" }"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            return new Response(
                    HttpStatus.CONFLICT,
                    ContentType.JSON,
                    "{ \"message\": \""+ e.getMessage() + "\" }"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }

    public List<Card> parseCardList(String jsonBody) throws IOException {
        // Deserialize the JSON array into a List of Package objects
        return this.getObjectMapper().readValue(jsonBody, this.getObjectMapper().getTypeFactory().constructCollectionType(List.class, Card.class));
    }
}
