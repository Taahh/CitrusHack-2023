package dev.taah.oursearch.problems.impl;

import dev.taah.oursearch.docker.DockerExecution;
import dev.taah.oursearch.problems.AbstractProblem;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 3:18 AM [30-04-2023]
 */
public class SearchInRotatedArray extends AbstractProblem {

    public SearchInRotatedArray() {
        super("Search in Rotated Sorted Array", 33);
        this.templates().put(DockerExecution.Language.PYTHON, readResource("problems/python/33-search-in-rotated-sorted-array.py"));
        this.templates().put(DockerExecution.Language.JAVA, readResource("problems/java/33-search-in-rotated-sorted-array.java"));
    }

}
