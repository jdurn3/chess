package server.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import service.UserService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinGame.class), session);
            case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, ObserveGame.class), session);
            case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMove.class), session);
            case LEAVE -> leave(new Gson().fromJson(message, Leave.class), session);
            case RESIGN -> resign(new Gson().fromJson(message, Resign.class), session);
        }
    }

    private void joinPlayer(JoinGame command, Session session) throws IOException, DataAccessException {
        String authToken = command.getAuthString();
        int gameID = command.getGameID();
        ChessGame.TeamColor teamColor = command.getPlayerColor();

        if (invalidAuth(authToken)) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid User");
            session.getRemote().sendString(new Gson().toJson(error));
        }

        else if (invalidGameID(gameID)) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Game");
            session.getRemote().sendString(new Gson().toJson(error));
        }

        else {

//            GameData gameData = Server.gameDAO.getGame(gameID);
//            if (!Server.gameDAO.checkPlayer(gameData, teamColor)) {
//                ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Color");
//                session.getRemote().sendString(new Gson().toJson(error));
//            } else {

                connections.add(authToken, session);
                connections.addPlayerToGame(authToken, gameID);

                AuthData user = Server.authDAO.getAuth(authToken);
                String username = user.username();

                ChessGame game = Server.gameDAO.getGame(gameID).game();

                // Send LOAD_GAME message to the root client
                LoadGameMessage loadGameMessage = new LoadGameMessage(game);
                session.getRemote().sendString(new Gson().toJson(loadGameMessage));

                // Construct the Notification message
                String notificationMessage = username + " joined as " + teamColor;
                Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);

                // Broadcast the notification to all players in the same game, excluding the joined player
                connections.broadcast(authToken, notification);
        }
    }

    private void joinObserver(ObserveGame command, Session session) throws IOException, DataAccessException {
        String authToken = command.getAuthString();
        int gameID = command.getGameID();

        if (invalidAuth(authToken)) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid User");
            session.getRemote().sendString(new Gson().toJson(error));
        }

        else if (invalidGameID(gameID)) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Game");
            session.getRemote().sendString(new Gson().toJson(error));
        }

        else {

            connections.add(authToken, session);
            connections.addPlayerToGame(authToken, gameID);

            AuthData user = Server.authDAO.getAuth(authToken);
            String username = user.username();

            ChessGame game = Server.gameDAO.getGame(gameID).game();

            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            session.getRemote().sendString(new Gson().toJson(loadGameMessage));

            // Construct the Notification message
            String notificationMessage = username + " joined as an observer";
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);

            // Broadcast the notification to all players in the same game, excluding the joined player
            connections.broadcast(authToken, notification);
        }
    }

    private void makeMove(MakeMove command, Session session) throws IOException {

        // Assuming connections.get() returns a session based on the authToken
        //Session session = connections.get(authToken);
//        if (session != null) {
//            String message = String.format("%s made a move", authToken);
//            ServerMessage serverMessage = new ServerMessage(ServerMessageType.NOTIFICATION);
//            serverMessage.setMessage(message);
//            connections.broadcast(authToken, serverMessage);
//        } else {
//            throw new IOException("Session not found for user: " + authToken);
//        }
    }

    private void leave(Leave command, Session session) throws IOException {
        String authToken = command.getAuthString();
        int gameID = command.getGameID();
        connections.removePlayerFromGame(authToken, gameID);

        String message = String.format("%s left the game", authToken);
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        //serverMessage.setMessage(message);
        connections.broadcast(authToken, serverMessage);
    }

    private void resign(Resign command, Session session) throws IOException {
        String authToken = command.getAuthString();
        connections.remove(authToken);
        String message = String.format("%s resigned from the game", authToken);
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        //serverMessage.setMessage(message);
        connections.broadcast(authToken, serverMessage);
    }

    private boolean invalidAuth (String authToken) throws DataAccessException, IOException {
        return Server.authDAO.getAuth(authToken) == null;
    }

    private boolean invalidGameID (int gameID) throws DataAccessException {
        return Server.gameDAO.getGame(gameID) == null;
    }
}

