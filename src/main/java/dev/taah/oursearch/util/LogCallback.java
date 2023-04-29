package dev.taah.oursearch.util;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 11:21 AM [29-04-2023]
 */

@Accessors(fluent = true)
@Getter
public class LogCallback extends ResultCallback.Adapter<Frame> {
    private final StringBuffer buffer = new StringBuffer();
    private boolean errored = false;

    @Override
    public void onNext(Frame object) {
        buffer.append(new String(object.getPayload()));
        if (Objects.requireNonNull(object.getStreamType()) == StreamType.STDERR) {
            errored = true;
        }
    }

    public Pair<Boolean, String> result() {
        String result = this.toString();
        return Pair.of(this.errored, result);
    }

    @Override
    public String toString() {
        try {
            awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return buffer.toString();
    }
}
