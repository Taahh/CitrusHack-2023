package dev.taah.oursearch.web;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.Gson;
import dev.taah.oursearch.OurSearch;
import dev.taah.oursearch.room.GameRoom;
import dev.taah.oursearch.room.RoomManager;
import dev.taah.oursearch.session.UserSession;
import org.apache.http.HttpHeaders;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Taah
 * @project citrus-2023-backend
 * @since 10:03 PM [29-04-2023]
 */

@RestController
public class RoomController {

    private static final Gson GSON = new Gson();

    @PutMapping(value = "/api/rooms/create", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> putRoom(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody String json) {
        if (!auth.equals(WebService.authKey())) {
            return new ResponseEntity<>("Invalid authorization key", HttpStatus.FORBIDDEN);
        }

        final JSONObject object = new JSONObject(json);

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
                    gameRoom.sessions().remove(uuid);
                    //TODO: Send socket message or redis message to game room to update
                }
            } else {
                if (!confirm) {
                    return new ResponseEntity<>("Are you sure you want to leave the game room?", HttpStatus.SEE_OTHER);
                }
                gameRoom.members().remove(uuid);
                gameRoom.sessions().remove(uuid);
                //TODO: Send socket message or redis message to game room to update
            }
        }

        gameRoom = new GameRoom(UUID.randomUUID());
        gameRoom.owner(uuid);
        gameRoom.roomName(username + "'s Room!");
        gameRoom.sessions().put(uuid, new UserSession(uuid));
        RoomManager.addGameRoom(gameRoom);

        return new ResponseEntity<>(gameRoom.roomId().toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/api/rooms/join/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> joinRoom(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody String json) {
        if (!auth.equals(WebService.authKey())) {
            return new ResponseEntity<>("Invalid authorization key", HttpStatus.FORBIDDEN);
        }

        final JSONObject object = new JSONObject(json);
        final String gameCode = object.getString("gameCode");
        final UUID joining = UUID.fromString(object.getString("joiningId"));
        final boolean confirm = object.has("confirm") && object.getBoolean("confirm");

        final GameRoom gameRoom = RoomManager.getGameRoom(gameCode);
        if (gameRoom == null) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }

        final GameRoom userGameRoom = RoomManager.findRoom(joining);

        if (userGameRoom != null) {
            if (userGameRoom.owner().equals(joining)) {
                if (!confirm) {
                    return new ResponseEntity<>(userGameRoom.members().isEmpty() ? "This action will delete the game room" : "This action will promote the room owner to the first person who joined.", HttpStatus.SEE_OTHER);
                }
                if (userGameRoom.members().isEmpty())
                {
                    RoomManager.remove(userGameRoom.roomId());
                } else {
                    userGameRoom.owner(userGameRoom.members().get(0));
                    userGameRoom.members().pop();
                    userGameRoom.sessions().remove(joining);
                    //TODO: Send socket message or redis message to game room to update
                }
            } else {
                if (!confirm) {
                    return new ResponseEntity<>("Are you sure you want to leave the game room?", HttpStatus.SEE_OTHER);
                }
                userGameRoom.members().remove(joining);
                userGameRoom.sessions().remove(joining);
                //TODO: Send socket message or redis message to game room to update
            }
        }

        gameRoom.members().add(joining);
        gameRoom.sessions().put(joining, new UserSession(joining));
        //TODO: Send socket msg / redis msg saying player joined
        return new ResponseEntity<>(gameRoom.roomId().toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/rooms/find/{uid}")
    public ResponseEntity<String> findRoom(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @PathVariable String uid) {
        if (!auth.equals(WebService.authKey())) {
            return new ResponseEntity<>("Invalid authorization key", HttpStatus.FORBIDDEN);
        }

        final GameRoom gameRoom = RoomManager.findRoom(UUID.fromString(uid));
        return new ResponseEntity<>(gameRoom == null ? "Not found" : gameRoom.roomId().toString(), gameRoom == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping(value = "/api/rooms/exists/{roomId}")
    public ResponseEntity<String> roomExists(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @PathVariable String roomId) {
        if (!auth.equals(WebService.authKey())) {
            return new ResponseEntity<>("Invalid authorization key", HttpStatus.FORBIDDEN);
        }

        final GameRoom gameRoom = RoomManager.getGameRoom(UUID.fromString(roomId));

        return new ResponseEntity<>(gameRoom == null ? "N/A" : gameRoom.roomId().toString(), gameRoom == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping(value = "/api/rooms/{roomId}")
    public ResponseEntity<String> getRoomInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @PathVariable String roomId) {
        if (!auth.equals(WebService.authKey())) {
            final JSONObject res = new JSONObject();
            res.put("msg", "Invalid authorization key!");
            return new ResponseEntity<>(res.toString(), HttpStatus.FORBIDDEN);
        }

        final GameRoom gameRoom = RoomManager.getGameRoom(UUID.fromString(roomId));
        final JSONObject object = new JSONObject(GSON.toJson(gameRoom));
        try {
            object.put("owner", OurSearch.firebaseEnvironment().firebaseAuth().getUser(gameRoom.owner().toString()).getDisplayName());
            object.put("members", gameRoom.members().stream().map(uuid -> {
                try {
                    return OurSearch.firebaseEnvironment().firebaseAuth().getUser(uuid.toString()).getDisplayName();
                } catch (FirebaseAuthException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList()));
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(object.toString(), HttpStatus.OK);
    }
}
