package dev.taah.oursearch.room;

import com.google.common.collect.Maps;
import dev.taah.oursearch.session.UserSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 9:56 PM [29-04-2023]
 */

@Accessors(fluent = true)
@Getter
@Setter
@RequiredArgsConstructor
public class GameRoom {
    private final UUID roomId;
    private final String gameCode = RandomStringUtils.randomAlphabetic(12).toUpperCase();
    private final LinkedList<UUID> members = new LinkedList<>();
    private final Map<UUID, UserSession> sessions = Maps.newHashMap();
    private UUID owner;
    private String roomName;
}
