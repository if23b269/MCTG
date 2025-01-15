package at.technikum.http;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserApiTests {

    @BeforeEach
    public void setUp() {
        // Set up base URI for the API
        RestAssured.baseURI = "http://localhost:10001";
    }

    // Test case for user creation (Registration)
    @Test
    public void testCreateUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"Username\":\"kienboec\", \"Password\":\"daniel\"}")
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body("{\"Username\":\"altenhof\", \"Password\":\"markus\"}")
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body("{\"Username\":\"admin\", \"Password\":\"istrator\"}")
                .when()
                .post("/users")
                .then()
                .statusCode(201);
    }

    @Test
    public void testDeleteUser() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/users/kienboec")
                .then()
                .statusCode(200);
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/users/altenhof")
                .then()
                .statusCode(200);
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/users/admin")
                .then()
                .statusCode(200);
    }

    // Test case for failing user creation (Duplicate users)
    @Test
    public void testCreateDuplicateUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"Username\":\"kienboec\", \"Password\":\"daniel\"}")
                .when()
                .post("/users")
                .then()
                .statusCode(201); // First registration

        given()
                .contentType(ContentType.JSON)
                .body("{\"Username\":\"kienboec\", \"Password\":\"daniel\"}")
                .when()
                .post("/users")
                .then()
                .statusCode(409); // Duplicate user, expecting 4xx
    }

    // Test case for login
    @Test
    public void testLogin() {
        String token = given()
                .contentType(ContentType.JSON)
                .body("{\"Username\":\"kienboec\", \"Password\":\"daniel\"}")
                .when()
                .post("/sessions")
                .then()
                .statusCode(200)
                .extract().path("message");
                //.extract().asString();

        System.out.println("Received token: " + token);
    }

    // Test case for failed login (Incorrect credentials)
    @Test
    public void testLoginFailed() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"Username\":\"kienboec\", \"Password\":\"wrongpassword\"}")
                .when()
                .post("/sessions")
                .then()
                .statusCode(409); // 4xx for failed login
    }

    // Test case for creating packages
    @Test
    public void testCreatePackages() {
        String token = getTokenForAdmin();

        String msg = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\":10.0}]")
                .when()
                .post("/packages")
                .then()
                .statusCode(201)
                .extract().path("message");

        System.out.println("Received message: " + msg);
    }

    // Test case for acquiring packages (valid request)
    @Test
    public void testAcquirePackages() {
        String token = getTokenForUser("kienboec");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("[]")
                .when()
                .post("/transactions/packages")
                .then()
                .statusCode(201); // Package acquisition should succeed
    }

    // Test case for acquiring packages (insufficient funds)
    @Test
    public void testAcquirePackagesInsufficientFunds() {
        String token = getTokenForUser("kienboec");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("[]")
                .when()
                .post("/transactions/packages")
                .then()
                .statusCode(400); // Insufficient funds expected
    }

    // Helper function to get a token for a user (needed for subsequent tests)
    private String getTokenForUser(String username) {
        return username + "-mtcgToken";
    }

    // Helper function to get admin token
    private String getTokenForAdmin() {
        return "admin-mtcgToken";
    }
}
