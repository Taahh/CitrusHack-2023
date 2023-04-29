package dev.taah.oursearch;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.api.model.*;
import dev.taah.oursearch.docker.DockerEnvironment;
import dev.taah.oursearch.docker.DockerExecution;
import dev.taah.oursearch.util.LogCallback;
import dev.taah.oursearch.web.WebService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 9:26 AM [29-04-2023]
 */
public class OurSearch {
    private static final DockerEnvironment DOCKER_ENVIRONMENT = new DockerEnvironment();

    public static void main(String[] args) {
        SpringApplication.run(WebService.class, args);
        DOCKER_ENVIRONMENT.runDocker(dockerClient -> {
            DockerExecution execution = new DockerExecution(UUID.randomUUID(), "Taah", "if __name__ == \"__main__\":\n" +
                    "    print(\"aaa\")");
            Pair<Boolean, String> res = execution.execute(dockerClient, DockerExecution.Language.PYTHON);
            System.out.println(res);
        });
    }
}
