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

import java.util.HashMap;
import java.util.Map;
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
    }

    public int[] twoSum(int nums[], int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int difference = target - nums[i];
            if (map.containsKey(difference)) {
                return new int[]{map.get(difference), i};
            }
            map.put(nums[i], i);
        }
        return new int[0];
    }

    public static DockerEnvironment dockerEnvironment() {
        return DOCKER_ENVIRONMENT;
    }

    public static FirebaseEnvironment firebaseEnvironment() {
        return FIREBASE_ENVIRONMENT;
    }
}
