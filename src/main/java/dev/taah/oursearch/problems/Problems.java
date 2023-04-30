package dev.taah.oursearch.problems;

import dev.taah.oursearch.problems.impl.SearchInRotatedArray;
import dev.taah.oursearch.problems.impl.TwoSum;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 3:17 AM [30-04-2023]
 */

@Accessors(fluent = true)
@Getter
public enum Problems {
    TWO_SUM(0x01, TwoSum.class),
    SEARCH_IN_ROTATED_SORTED_ARRAY(33, SearchInRotatedArray.class);

    private final int identifier;
    private final Class<? extends AbstractProblem> problem;
    Problems(int identifier, Class<? extends AbstractProblem> problem) {
        this.identifier = identifier;
        this.problem = problem;
    }
}
