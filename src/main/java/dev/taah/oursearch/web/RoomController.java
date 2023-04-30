package dev.taah.oursearch.web;

import dev.taah.oursearch.room.GameRoom;
import dev.taah.oursearch.room.RoomManager;
import org.apache.http.HttpHeaders;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 10:03 PM [29-04-2023]
 */

@RestController
public class RoomController {

    @PutMapping(value = "/api/rooms/create", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> putRoom(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody String json) {
        if (!auth.equals(WebService.authKey())) {
            return new ResponseEntity<>("Invalid authorization key", HttpStatus.FORBIDDEN);
        }

        JSONObject object = new JSONObject(json);

        final UUID uuid = UUID.fromString(object.getString("uid"));
        final String username = object.getString("username");
        final boolean confirm = object.has("confirm") && object.getBoolean("confirm");

        GameRoom gameRoom = RoomManager.findRoom(uuid);

        if (gameRoom != null) {
            if (gameRoom.owner().equals(uuid)) {
                if (!confirm) {
                    return new ResponseEntity<>(gameRoom.members().isEmpty() ? "This action will delete the game room" : "This action will promote the room owner to the first person who joined.", HttpStatus.SEE_OTHER);
                }
                if (gameRoom.members().isEmpty())
                {
                    RoomManager.remove(gameRoom.roomId());
                } else {
                    gameRoom.owner(gameRoom.members().get(0));
                    gameRoom.members().pop();
                    //TODO: Send socket message or redis message to game room to update
                }
            } else {
                if (!confirm) {
                    return new ResponseEntity<>("Are you sure you want to leave the game room?", HttpStatus.SEE_OTHER);
                }
                gameRoom.members().remove(uuid);
                //TODO: Send socket message or redis message to game room to update
            }
        }

        gameRoom = new GameRoom(UUID.randomUUID());
        gameRoom.owner(uuid);
        gameRoom.roomName(username + "'s Room!");
        RoomManager.addGameRoom(gameRoom);

        return new ResponseEntity<>(gameRoom.roomId().toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/rooms/find/{uid}", consumes = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> findRoom(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestParam String uid) {
        if (!auth.equals(WebService.authKey())) {
            return new ResponseEntity<>("Invalid authorization key", HttpStatus.FORBIDDEN);
        }

        final GameRoom gameRoom = RoomManager.findRoom(UUID.fromString(uid));
        return new ResponseEntity<>(gameRoom == null ? "Not found" : gameRoom.roomId().toString(), gameRoom == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}
