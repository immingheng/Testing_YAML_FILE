package ibf2021.springboot.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.Base64;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import ibf2021.springboot.models.User;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class AuthService {

    private final static Logger logger = Logger.getLogger(AuthService.class.getName());

    // METHOD TO EXTRACT USER EMAIL AND NAME FROM JWT
    public User extractUserFromJWT(String jwt) {
        // jwt = "Bearer 3 parts token (Header.Body.Signature)"
        String requestBody = jwt.split(" ")[1];
        String base64EncodedBody = requestBody.split("\\.")[1];
        String body = new String(Base64.getDecoder().decode(base64EncodedBody));
        Reader reader = new StringReader(body);
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject data = jsonReader.readObject();
        String email = data.getString("https://www.my-cute-shop.vercel.app/email");
        String name = data.getString("https://www.my-cute-shop.vercel.app/name");
        User user = new User();
        user.setEmail(email);
        logger.info("USER'S EMAIL BASED ON JWT IS " + email);
        user.setName(name);
        logger.info("USER'S NAME BASED ON JWT IS " + name);
        return user;
    }

}
