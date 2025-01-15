package at.technikum.api.controller;

import at.technikum.DAL.DAO.Card;
import at.technikum.DAL.DAO.Deck;
import at.technikum.DAL.DAO.Package;
import at.technikum.DAL.DAO.User;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import at.technikum.persistence.dao.DeckDao;
import at.technikum.persistence.dao.PackageDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
public class DeckController extends Controller {
    private final DeckDao deckDao;

    public DeckController() {
        this.deckDao = new DeckDao();
        // Nur noch für die Dummy-JUnit-Tests notwendig. Stattdessen ein RepositoryPattern verwenden.

    }

    // GET /deck(:username
    public Response getDeck(String uuid)
    {
        try {
            Optional<Deck> deckO = this.deckDao.getByUsername(uuid);
            if (deckO.isPresent()) {
                this.deckDao.delete(deckO.get());
            } else {
                throw new IllegalArgumentException("Deck does not exist");
            }
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{ \"message\": \"Success\" }"
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

    public Response getDecks() {
        try {
            List<Deck> decks = this.deckDao.getAll();
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String userDataJSON = this.getObjectMapper().writeValueAsString(decks);

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
    public Response addDeck(Request request) {
        try {
            List <Card> cards = this.getObjectMapper().readValue(request.getBody(), this.getObjectMapper().getTypeFactory().constructCollectionType(List.class, Card.class));

            this.deckDao.saveWithCards(new Deck("Deck"), cards);

            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    "{ \"message\": \"Success\" }"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException | SQLException e) {
            return new Response(
                    HttpStatus.CONFLICT,
                    ContentType.JSON,
                    "{ \"message\": \""+ e.getMessage() + "\" }"
            );
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
