package dev.taah.oursearch.session;

import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import dev.taah.oursearch.problems.AbstractProblem;
import dev.taah.oursearch.problems.Problems;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 3:30 AM [30-04-2023]
 */

@Accessors(fluent = true)
@Getter
public class UserSession {
    private final UUID uuid;
    private final List<AbstractProblem> problemsList = Lists.newArrayList();

    public UserSession(UUID uuid) {
        this.uuid = uuid;
        for (Problems value : Problems.values()) {
            try {
                Class<? extends AbstractProblem> problem = value.problem();
                Constructor<? extends AbstractProblem> constructor = problem.getConstructor();
                constructor.setAccessible(true);
                problemsList.add(constructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
