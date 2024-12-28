package at.technikum.http;

import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.server.Response;
import at.technikum.httpserver.server.Service;
import at.technikum.httpserver.utils.Router;
import at.technikum.service.SessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

public class RouterTest {
    private static Router router;

    @BeforeEach
    void setup() {
        router = new Router();
    }

    @Test
    void simpleSessionRoute() throws SQLException, JsonProcessingException {
        Service mockedSessionService = Mockito.mock(SessionService.class);
        // Add the mocked session service to the router under the "/sessions" route
        router.addService("/user", mockedSessionService);

        // Resolve the service for the "/sessions" path using the router
        Service result = router.resolve("/user");

        // Assert that the resolved service is not null (i.e., it found the service for "/sessions")
        Assertions.assertNotNull(result);

        // Call the handleRequest method on the resolved service (passing null as a mock request)
        result.handleRequest(null);

        // Verify that handleRequest was called exactly once with null as the argument
        // The verification ensures that the service correctly handled the request
    }

    @Test
    void testResolveRouteWithMockService() {
        // Arrange: Mock a Service and set up the Router
        Service mockedSessionService = Mockito.mock(SessionService.class);

        // Define mock behavior for handleRequest
        Mockito.when(mockedSessionService.handleRequest(null))
                .thenReturn(new Response(HttpStatus.OK, ContentType.JSON, "Mocked Response"));

        // Add the mock service to the router
        router.addService("/sessions", mockedSessionService);

        // Act: Resolve the route
        Service result = router.resolve("/sessions");

        // Assert: Verify that the resolved route is not null and is the correct service
        Assertions.assertNotNull(result, "Resolved service should not be null");
        Assertions.assertSame(mockedSessionService, result, "Resolved service should match the registered service");

        // Call the handleRequest method and check the response
        Object response = result.handleRequest(null);
        Assertions.assertEquals("Mocked Response", response.toString(), "The response from handleRequest should match the mocked value");

        // Verify that handleRequest was called exactly once with the correct parameter
        Mockito.verify(mockedSessionService, Mockito.times(1)).handleRequest(null);
    }
}
