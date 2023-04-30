package dev.taah.oursearch;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import dev.taah.oursearch.docker.DockerEnvironment;
import dev.taah.oursearch.docker.DockerExecution;
import dev.taah.oursearch.firebase.FirebaseEnvironment;
import dev.taah.oursearch.problems.Problems;
import dev.taah.oursearch.web.WebService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.SpringApplication;

import java.util.UUID;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 9:26 AM [29-04-2023]
 */
public class OurSearch {
    private static final DockerEnvironment DOCKER_ENVIRONMENT = new DockerEnvironment();
    private static final FirebaseEnvironment FIREBASE_ENVIRONMENT = new FirebaseEnvironment();

    public static void main(String[] args) {
        SpringApplication.run(WebService.class, args);

        /*final Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(8081);

        final SocketIOServer server = new SocketIOServer(config);
        server.addEventListener("");*/

        DOCKER_ENVIRONMENT.runDocker(dockerClient -> {
            DockerExecution execution = new DockerExecution(UUID.randomUUID(), "Taah", "if __name__ == \"__main__\":\n" +
                    "    print(\"aaa\")");
            Pair<Boolean, String> res = execution.execute(dockerClient, DockerExecution.Language.PYTHON);
            System.out.println(res);
        });
    }

    public static DockerEnvironment dockerEnvironment() {
        return DOCKER_ENVIRONMENT;
    }

    public static FirebaseEnvironment firebaseEnvironment() {
        return FIREBASE_ENVIRONMENT;
    }
}
