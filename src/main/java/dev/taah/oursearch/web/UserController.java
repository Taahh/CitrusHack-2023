package dev.taah.oursearch.web;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import dev.taah.oursearch.OurSearch;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 4:14 PM [29-04-2023]
 */

//@CrossOrigin
@RestController
public class UserController {

    @PutMapping(value = "/api/users/create", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> putUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody String json) {
        if (!auth.equals(WebService.authKey())) {
            return new ResponseEntity<>("Invalid authorization key", HttpStatus.FORBIDDEN);
        }

        JSONObject object = new JSONObject(json);
        String email = object.getString("email");
        String username = object.getString("username");
        String password = object.getString("password");

        try {
            if (OurSearch.firebaseEnvironment().firebaseAuth().getUserByEmail(email) != null) {
                return new ResponseEntity<>("User already exists!", HttpStatus.SEE_OTHER);
            }
        } catch (FirebaseAuthException e) {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setDisplayName(username)
                    .setPassword(password)
                    .setEmailVerified(true)
                    .setUid(UUID.randomUUID().toString());

            UserRecord record;
            try {
                record = OurSearch.firebaseEnvironment().firebaseAuth().createUser(request);
            } catch (FirebaseAuthException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("USER CREATED! " + record.getUid());
        }

        return new ResponseEntity<>("User created!", HttpStatus.OK);
    }

    @GetMapping("/api/users/get/{uid}")
    public ResponseEntity<String> getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @PathVariable String uid) {
        if (!auth.equals(WebService.authKey())) {
            return new ResponseEntity<>("Invalid authorization key", HttpStatus.FORBIDDEN);
        }

        final JSONObject object = new JSONObject();

        try {
            final UserRecord record = OurSearch.firebaseEnvironment().firebaseAuth().getUser(uid);
            object.put("username", record.getDisplayName());
            object.put("email", record.getEmail());
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(object.toString(), HttpStatus.OK);
    }
}
