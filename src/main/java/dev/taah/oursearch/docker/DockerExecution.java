package dev.taah.oursearch.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Mount;
import com.github.dockerjava.api.model.MountType;
import dev.taah.oursearch.util.LogCallback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 12:11 PM [29-04-2023]
 */

@AllArgsConstructor
public class DockerExecution {
    private static final File RUNS = new File("run");

    static {
        if (!RUNS.exists()) RUNS.mkdir();
    }
    private final UUID uuid;
    private final String username;
    private final String code;

    public Pair<Boolean, String> execute(DockerClient client, Language language) {
        File file = new File(RUNS, this.uuid.toString() + "-" + language + "." + language.extension);
        if (file.exists()) {
            file.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(this.code);
                    writer.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String[] path = file.getAbsolutePath().split("/");
        String mounthPath = StringUtils.join(path, "/", 0, path.length - 1);
        CreateContainerResponse response = client.createContainerCmd("python:3")
                .withHostConfig(HostConfig.newHostConfig().withMounts(Collections.singletonList(new Mount().withTarget("/src").withType(MountType.BIND).withSource(mounthPath))))
                .withCmd("python3", "/src/" + file.getName())
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .exec();
        client.startContainerCmd(response.getId()).exec();

        LogCallback cmd = client.logContainerCmd(response.getId()).withTailAll().withFollowStream(true).withStdOut(true).withStdErr(true).exec(new LogCallback());
        client.killContainerCmd(response.getId()).exec();
        client.removeContainerCmd(response.getId()).exec();
        file.delete();
        return cmd.result();
    }

    @Accessors(fluent = true)
    @Getter
    public enum Language {
        PYTHON("py");

        private final String extension;
        Language(String extension) {
            this.extension = extension;
        }
    }
}
