package dev.taah.oursearch.problems;

import com.google.common.collect.Maps;
import dev.taah.oursearch.OurSearch;
import dev.taah.oursearch.docker.DockerExecution;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 3:16 AM [30-04-2023]
 */

@Accessors(fluent = true)
@Getter
@RequiredArgsConstructor
public abstract class AbstractProblem {
    private final String problemName;
    private final int problemId;
    private final Map<DockerExecution.Language, String> templates = Maps.newHashMap();

    @SneakyThrows
    protected String readFileInputStream(FileInputStream stream) {
        return IOUtils.toString(stream, StandardCharsets.UTF_8.name());
    }

    @SneakyThrows
    protected String readResource(String resource) {
        return IOUtils.toString(OurSearch.class.getResourceAsStream("/" + resource), StandardCharsets.UTF_8.name());
    }

}
