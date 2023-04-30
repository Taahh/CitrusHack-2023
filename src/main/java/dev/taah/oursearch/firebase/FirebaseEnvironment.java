package dev.taah.oursearch.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import dev.taah.oursearch.OurSearch;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 1:41 PM [29-04-2023]
 */

@Accessors(fluent = true)
@Getter
public class FirebaseEnvironment {
    private final FirebaseApp firebaseApp;
    private final FirebaseOptions firebaseOptions;
    private final FirebaseAuth firebaseAuth;

    public FirebaseEnvironment() {
        try {
            this.firebaseOptions = FirebaseOptions.builder()
                    .setDatabaseUrl("https://citrus-hack-6b50e-default-rtdb.firebaseio.com/")
                    .setCredentials(GoogleCredentials.fromStream(OurSearch.class.getResourceAsStream("/google-oauth-token.json"))).build();
            this.firebaseApp = FirebaseApp.initializeApp(this.firebaseOptions);
            this.firebaseAuth = FirebaseAuth.getInstance(this.firebaseApp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
