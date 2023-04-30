package dev.taah.oursearch.room;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.UUID;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 10:39 PM [29-04-2023]
 */
public class RoomManager {
    private static final Map<UUID, GameRoom> GAME_ROOMS = Maps.newHashMap();

    public static Map<UUID, GameRoom> gameRooms() {
        return GAME_ROOMS;
    }

    public static void addGameRoom(GameRoom room) {
        gameRooms().put(room.roomId(), room);
    }

    public static GameRoom getGameRoom(UUID roomId) {
        return gameRooms().get(roomId);
    }

    public static GameRoom getGameRoom(String gameCode) {
        return gameRooms().values().stream().filter(room -> room.gameCode().equalsIgnoreCase(gameCode)).findFirst().orElse(null);
    }

    public static void remove(UUID roomId) {
        gameRooms().remove(roomId);
    }

    public static GameRoom ownsRoom(UUID userId) {
        return gameRooms().values().stream().filter(room -> room.owner().equals(userId)).findFirst().orElse(null);
    }

    public static GameRoom findRoom(UUID userId) {
        return gameRooms().values().stream().filter(room -> room.owner().equals(userId) || room.members().contains(userId)).findFirst().orElse(null);
    }
}
