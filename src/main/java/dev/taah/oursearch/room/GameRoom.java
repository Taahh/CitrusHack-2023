package dev.taah.oursearch.room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
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
    private UUID owner;

    private final LinkedList<UUID> members = new LinkedList<>();
    private String roomName;
}
