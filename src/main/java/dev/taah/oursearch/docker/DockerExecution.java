package dev.taah.oursearch.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Mount;
import com.github.dockerjava.api.model.MountType;
import dev.taah.oursearch.OurSearch;
import dev.taah.oursearch.util.LogCallback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public Pair<Boolean, String> execute(DockerClient client, Language language, int problemId) {
        File file = new File(RUNS, this.uuid.toString() + "-" + language + "." + language.extension);
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    switch (problemId) {
                        case 1 -> {
                            writer.write(this.code);
                            writer.append(System.lineSeparator());
                            String data = IOUtils.toString(OurSearch.class.getResourceAsStream(language == Language.PYTHON ? "/problems/python/solutions/01-two-sum.py" : "/problems/java/solutions/01-two-sum.java"), StandardCharsets.UTF_8.name());
                            writer.write(data);
                        }
                        case 33 -> {
                            writer.write(this.code);
                            writer.append(System.lineSeparator());
                            String data = IOUtils.toString(OurSearch.class.getResourceAsStream("/" + "problems/python/solutions/33-search-in-rotated-sorted-array.py"), StandardCharsets.UTF_8.name());
                            writer.write(data);
                        }
                    }
                    writer.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String[] path = file.getAbsolutePath().split("/");
        String mounthPath = StringUtils.join(path, "/", 0, path.length - 1);
        CreateContainerResponse response = client.createContainerCmd(language == Language.PYTHON ? "python:3" : "openjdk:17-oracle")
                .withHostConfig(HostConfig.newHostConfig().withMounts(Collections.singletonList(new Mount().withTarget("/src").withType(MountType.BIND).withSource(mounthPath))))
                .withCmd(language == Language.PYTHON ? new String[]{"python3", "/src/" + file.getName()} : new String[]{"sh", "-c", "javac /src/" + file.getName() + " && echo 'hi' && java -ea -cp /src Run"})
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .exec();
        client.startContainerCmd(response.getId()).exec();

        LogCallback cmd = client.logContainerCmd(response.getId()).withTailAll().withFollowStream(true).withStdOut(true).withStdErr(true).exec(new LogCallback());
        try {
            client.killContainerCmd(response.getId()).exec();
        } catch (ConflictException ignored) {

        }
        client.removeContainerCmd(response.getId()).exec();
//        file.delete();
        return cmd.result();
    }

    @Accessors(fluent = true)
    @Getter
    public enum Language {
        PYTHON("py"),
        JAVA("java");

        private final String extension;

        Language(String extension) {
            this.extension = extension;
        }
    }
}
