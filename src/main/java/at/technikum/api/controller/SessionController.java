package at.technikum.api.controller;

import at.technikum.DAL.DAO.User;
import at.technikum.DAL.SessionData;
import at.technikum.DAL.UserData;
import at.technikum.httpserver.http.ContentType;
import at.technikum.httpserver.http.HttpStatus;
import at.technikum.httpserver.server.Request;
import at.technikum.httpserver.server.Response;
import at.technikum.httpserver.server.Session;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SessionController extends Controller {
    private SessionData sessionData;
    private final String SECRET_KEY = "B4EFA2836BDED9533BA6E7AD51C4A";

    public SessionController() {

        // Nur noch für die Dummy-JUnit-Tests notwendig. Stattdessen ein RepositoryPattern verwenden.
        this.sessionData = new SessionData();
    }

    // POST /weather
    public Response addSession(Request request) {
        try {

            // request.getBody() => "{ \"id\": 4, \"city\": \"Graz\", ... }
            User user = this.getObjectMapper().readValue(request.getBody(), User.class);

            //JSON Web Token
            String token = generateJWT(user.getUsername());
            Session session = new Session(user, token);
            this.sessionData.addSession(session);


            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{ message: \"Success - with generated token for the user, here: " + user.getUsername() + "-" + token + "\" }"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }

    public String generateJWT(String username) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 3600000; // Token valid for 1 hour
        Date exp = new Date(expMillis);

        // Create the JWT header
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        // Create the JWT payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", username);
        payload.put("exp", exp.getTime() / 1000);
        payload.put("iat", nowMillis / 1000);

        // Encode the header and payload
        String headerEncoded = base64UrlEncode(toJson(header));
        String payloadEncoded = base64UrlEncode(toJson(payload));

        // Create the signature
        String signature = hmacSHA256(headerEncoded + "." + payloadEncoded, SECRET_KEY);
        String signatureEncoded = base64UrlEncode(signature);

        // Combine all parts to create the JWT
        return headerEncoded + "." + payloadEncoded + "." + signatureEncoded;
    }

    private String toJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }
            first = false;
        }
        json.append("}");
        return json.toString();
    }

    private String base64UrlEncode(String input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    private String hmacSHA256(String data, String key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create HMAC SHA256 signature", e);
        }
    }

    public static void handleRequest(String method, String path, BufferedWriter out) throws IOException {
        if (method.equals("GET") && path.equals("/sessions")) {
            sendSessionList(out);
        } else {
            sendNotFound(out);
        }
    }

    private static void sendSessionList(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: application/json\r\n");
        out.write("\r\n");
        out.write("[{\"sessionId\": \"abc123\", \"userId\": 1}, {\"sessionId\": \"xyz789\", \"userId\": 2}]");
        out.flush();
    }

    private static void sendNotFound(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 404 Not Found\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("404 - Session Not Found");
        out.flush();
    }
}
