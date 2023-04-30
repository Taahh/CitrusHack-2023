package dev.taah.oursearch.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Consumer;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 9:41 AM [29-04-2023]
 */

@Accessors(fluent = true)
@Getter
public class DockerEnvironment {
    private final DockerClientConfig config;
    private final DockerHttpClient httpClient;

    public DockerEnvironment() {
        this.config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")
                .withDockerTlsVerify(false)
                .build();
        this.httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(this.config.getDockerHost())
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
    }

    public void runDocker(Consumer<DockerClient> consumer) {
        try (DockerClient client = DockerClientImpl.getInstance(this.config, this.httpClient)) {
            consumer.accept(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
