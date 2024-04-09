package server.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    final ConcurrentHashMap<String, Integer> gamesToPlayers = new ConcurrentHashMap<>();


    public void add(String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void addPlayerToGame(String authToken, int gameID) {
        gamesToPlayers.put(authToken, gameID);
    }

    public void removePlayerFromGame(String authToken, int gameId) {
        gamesToPlayers.remove(authToken);
    }
    public void broadcast(String authToken, ServerMessage servermessage) throws IOException {
        var removeList = new ArrayList<Connection>();

        // Get the game ID associated with the excluded user
        Integer gameId = gamesToPlayers.get(authToken);

        // Iterate over connections to find those in the same game
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                // Check if the user is in the same game and exclude the specified user
                if (gamesToPlayers.get(c.authToken).equals(gameId) && !c.authToken.equals(authToken)) {
                    c.send(new Gson().toJson(servermessage));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }
}