package at.technikum.http;

import at.technikum.api.controller.UserController;
import at.technikum.DAL.DAO.User;
import at.technikum.persistence.dao.UserDao;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController; // This will automatically inject mocked UserDao

    @Mock
    private UserDao userDao; // Mocking the UserDao

    @Mock
    private Request request; // Mocking the request object (for addUser and updateUser)

    @Mock
    private Response response; // Mocking the response object (if needed)

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setName("Test User");
        testUser.setBio("This is a test bio.");
    }

    // Test getUser method
    @Test
    public void testGetUser_Success() throws JsonProcessingException {
        // Arrange
        String username = "testUser";
        when(userDao.getByUsername(username)).thenReturn(Optional.of(testUser));

        // Act
        Response response = userController.getUser(username);

        // Assert
        assertEquals(HttpStatus.OK.code, response.getStatus());
        assertTrue(response.getContent().contains("testUser"));
    }

    @Test
    public void testGetUser_UserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userDao.getByUsername(username)).thenReturn(Optional.empty());

        // Act
        Response response = userController.getUser(username);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.code, response.getStatus());
        assertTrue(response.getContent().contains("User does not exist"));
    }

    // Test addUser method
    @Test
    public void testAddUser_Success() throws JsonProcessingException {
        // Arrange
        when(request.getBody()).thenReturn("{\"username\":\"newUser\",\"name\":\"New User\",\"bio\":\"New user bio\"}");
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setName("New User");
        newUser.setBio("New user bio");

        when(userDao.getAll()).thenReturn(java.util.Collections.emptyList()); // No user exists
        doNothing().when(userDao).save(any(User.class)); // Simulate saving the user

        // Act
        Response response = userController.addUser(request);

        // Assert
        assertEquals(HttpStatus.CREATED.code, response.getStatus());
        assertTrue(response.getContent().contains("Success"));
    }

    @Test
    public void testAddUser_UserAlreadyExists() throws JsonProcessingException {
        // Arrange
        when(request.getBody()).thenReturn("{\"username\":\"testUser\",\"name\":\"Test User\",\"bio\":\"This is a test bio.\"}");
        when(userDao.getAll()).thenReturn(java.util.Collections.singletonList(testUser)); // user already exists

        // Act
        Response response = userController.addUser(request);

        // Assert
        assertEquals(HttpStatus.CONFLICT.code, response.getStatus());
        assertTrue(response.getContent().contains("User already exists"));
    }

    // Test deleteUser method
    @Test
    public void testDeleteUser_Success() {
        // Arrange
        String username = "testUser";
        when(userDao.getByUsername(username)).thenReturn(Optional.of(testUser));
        doNothing().when(userDao).delete(testUser);

        // Act
        Response response = userController.deleteUser(username);

        // Assert
        assertEquals(HttpStatus.OK.code, response.getStatus());
        assertTrue(response.getContent().contains("success"));
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userDao.getByUsername(username)).thenReturn(Optional.empty());

        // Act
        Response response = userController.deleteUser(username);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.code, response.getStatus());
        assertTrue(response.getContent().contains("User does not exist"));
    }
}

