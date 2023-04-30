package dev.taah.oursearch.problems.impl;

import dev.taah.oursearch.docker.DockerExecution;
import dev.taah.oursearch.problems.AbstractProblem;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 3:18 AM [30-04-2023]
 */
public class TwoSum extends AbstractProblem {

    public TwoSum() {
        super("Two Sum", 0x01);
        this.templates().put(DockerExecution.Language.PYTHON, readResource("problems/python/01-two-sum.py"));
        this.templates().put(DockerExecution.Language.JAVA, readResource("problems/java/01-two-sum.java"));
    }

}
