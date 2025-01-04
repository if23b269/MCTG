package at.technikum.api.controller;

import at.technikum.DAL.DAO.Card;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import at.technikum.persistence.dao.CardDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class CardController extends Controller {
    private final CardDao cardDao;

    public CardController() {
        this.cardDao = new CardDao();
        // Nur noch für die Dummy-JUnit-Tests notwendig. Stattdessen ein RepositoryPattern verwenden.

    }

    // GET /card(:username
    public Response getCard(String username)
    {
        try {
            Card cardData = this.cardDao.getByUsername(username).get();
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String userDataJSON = this.getObjectMapper().writeValueAsString(cardData);

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

    public Response getCards() {
        try {
            List<Card> cardData = this.cardDao.getAll();
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String userDataJSON = this.getObjectMapper().writeValueAsString(cardData);

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
    public Response addCard(Request request) {
        try {
            List<Card> cardData = this.cardDao.getAll();
            // request.getBody() => "{ \"id\": 4, \"city\": \"Graz\", ... }
            Card card = this.getObjectMapper().readValue(request.getBody(), Card.class);
            //List<User> users = this.getUserDAL().getUserData();
            //users.add(user);
            /*for (int i = 0; i < users.size(); i++) {
                System.out.println(users.get(i).getUsername());
            }*/
            /*UserData userData = this.getUserDAL();
            userData.addUser(user);
            userData.setUserData(userData.getUserData());
            this.setUserDAL(userData);*/
            if(cardData.contains(card)) {
                throw new IllegalArgumentException("Card already exists");
            }

            this.cardDao.save(card);


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
}
