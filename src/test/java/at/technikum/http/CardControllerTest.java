package at.technikum.http;

import at.technikum.api.controller.CardController;
import at.technikum.DAL.DAO.Card;
import at.technikum.game.CardType;
import at.technikum.persistence.dao.CardDao;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardControllerTest {

    @InjectMocks
    private CardController cardController;  // The controller to test, with dependencies injected

    @Mock
    private CardDao cardDao;  // Mock the CardDao class

    @Mock
    private Request request;  // Mock the Request object (used for addCard)

    private Card testCard;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        testCard = new Card();
        testCard.setName("testCard");
        testCard.setType(CardType.MONSTER);
        testCard.setDamage(100.0);
    }

    // Test getCard method
    @Test
    public void testGetCard_Success() throws JsonProcessingException {
        // Arrange
        String username = "testUser";
        when(cardDao.getByUsername(username)).thenReturn(Optional.of(testCard));

        // Act
        Response response = cardController.getCard(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatus());
        assertTrue(response.getContent().contains("testCard"));
    }

    @Test
    public void testGetCard_CardNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(cardDao.getByUsername(username)).thenReturn(Optional.empty());

        // Act
        Response response = cardController.getCard(username);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertTrue(response.getContent().contains("Internal DB Error"));
    }

    @Test
    public void testGetCard_ExceptionHandling() throws JsonProcessingException {
        // Arrange
        String username = "testUser";
        when(cardDao.getByUsername(username)).thenThrow(new RuntimeException("Database error"));

        // Act
        Response response = cardController.getCard(username);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertTrue(response.getContent().contains("Internal Server Error"));
    }

    // Test getCards method
    @Test
    public void testGetCards_Success() throws JsonProcessingException {
        // Arrange
        List<Card> cardList = List.of(testCard);
        when(cardDao.getAll()).thenReturn(cardList);

        // Act
        Response response = cardController.getCards();

        // Assert
        assertEquals(HttpStatus.OK.code, response.getStatus());
    }

    @Test
    public void testGetCards_EmptyList() throws JsonProcessingException {
        // Arrange
        when(cardDao.getAll()).thenReturn(List.of());

        // Act
        Response response = cardController.getCards();

        // Assert
        assertEquals(HttpStatus.OK.code, response.getStatus());
        //assertTrue(response.getContent().isEmpty());
    }

    // Test addCard method
    @Test
    public void testAddCard_Success() throws JsonProcessingException {
        // Arrange
        when(request.getBody()).thenReturn("{\"username\":\"testUser\",\"cardName\":\"testCard\",\"cardType\":\"Monster\",\"damage\":100.0}");
        Card newCard = new Card();
        testCard.setName("testCard");
        testCard.setType(CardType.MONSTER);
        newCard.setDamage(100.0);

        when(cardDao.getAll()).thenReturn(List.of());  // No existing cards
        doNothing().when(cardDao).save(any(Card.class));  // Simulate card save

        // Act
        Response response = cardController.addCard(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertTrue(response.getContent().contains("Success"));
    }

    @Test
    public void testAddCard_CardAlreadyExists() throws JsonProcessingException {
        // Arrange
        when(request.getBody()).thenReturn("{\"username\":\"testUser\",\"cardName\":\"testCard\",\"cardType\":\"Monster\",\"damage\":100.0}");
        List<Card> cardList = List.of(testCard);
        when(cardDao.getAll()).thenReturn(cardList);  // Card already exists

        // Act
        Response response = cardController.addCard(request);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatus());
        assertTrue(response.getContent().contains("Card already exists"));
    }

    @Test
    public void testAddCard_ExceptionHandling() throws JsonProcessingException {
        // Arrange
        when(request.getBody()).thenReturn("{\"username\":\"testUser\",\"cardName\":\"testCard\",\"cardType\":\"Monster\",\"damage\":100.0}");
        when(cardDao.getAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        Response response = cardController.addCard(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertTrue(response.getContent().contains("Internal Server Error"));
    }
}

