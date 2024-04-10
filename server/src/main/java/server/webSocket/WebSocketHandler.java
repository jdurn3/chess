package server.webSocket;

import chess.*;
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
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
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
            return;
        }

        if (invalidGameID(gameID)) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Game");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }

        GameData gameData = Server.gameDAO.getGame(gameID);
        ChessGame game = gameData.game();


        AuthData user = Server.authDAO.getAuth(authToken);
        String username = user.username();

        ChessGame.TeamColor playerColor = getUserColor(username, gameData);

        if ((playerColor == null) || (!playerColor.equals(teamColor))) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Wrong Color");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
            connections.add(authToken, session);
            connections.addPlayerToGame(authToken, gameID);


            // Send LOAD_GAME message to the root client
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            session.getRemote().sendString(new Gson().toJson(loadGameMessage));

            // Construct the Notification message
            String notificationMessage = username + " joined as " + teamColor;
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);

            // Broadcast the notification to all players in the same game, excluding the joined player
            connections.broadcast(authToken, notification);

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

    private void makeMove(MakeMove command, Session session) throws IOException, DataAccessException, InvalidMoveException {
        String authToken = command.getAuthString();
        int gameID = command.getGameID();

        ChessMove move = command.getMove();

        if (invalidAuth(authToken)) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid User");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }

        if (invalidGameID(gameID)) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Game");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }

        GameData gameData = Server.gameDAO.getGame(gameID);
        ChessGame game = gameData.game();

        AuthData user = Server.authDAO.getAuth(authToken);
        String username = user.username();

        ChessGame.TeamColor color = getUserColor(username, gameData);

        if (color == null) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Can't make move as Observer.");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }

        ChessPosition position = move.getStartPosition();
        ChessPiece.PieceType piece = game.getBoard().getPiece(position).getPieceType();
        ChessMove newMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), piece);
        ChessGame.TeamColor pieceColor = game.getBoard().getPiece(position).getTeamColor();

        if (!pieceColor.equals(color)) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Not your team's piece.");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }

        try {
            // Attempt to make the move
            game.makeMove(move);
            Server.gameDAO.updateGame(gameID, game);

            // If the move is valid, proceed to load the game and broadcast the update
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            connections.notify(gameID, loadGameMessage);
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username);
            connections.broadcast(authToken, notification);
        } catch (InvalidMoveException e) {
            // If an InvalidMoveException is caught, the move is invalid
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Move");
            session.getRemote().sendString(new Gson().toJson(error));
        }
    }

    private void leave(Leave command, Session session) throws IOException, DataAccessException {
        String authToken = command.getAuthString();
        int gameID = command.getGameID();

        AuthData user = Server.authDAO.getAuth(authToken);
        String username = user.username();
        String message = String.format("%s left the game", username);
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(authToken, notification);
        Server.gameDAO.leaveGame(username, gameID);
        connections.removePlayerFromGame(authToken, gameID);
    }

    private void resign(Resign command, Session session) throws IOException, DataAccessException {
        String authToken = command.getAuthString();
        int gameID = command.getGameID();
        AuthData user = Server.authDAO.getAuth(authToken);
        String username = user.username();

        if (invalidGameID(gameID)) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Game");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
        GameData gameData = Server.gameDAO.getGame(gameID);
        ChessGame.TeamColor playerColor = getUserColor(username, gameData);

        if (playerColor == null) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Observer cannot resign");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }

        String message = String.format("%s resigned from the game", username);
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.notify(gameID, notification);
        Server.gameDAO.deleteGame(gameID);
        connections.removeGameFromPlayers(gameID);
    }

    private boolean invalidAuth (String authToken) throws DataAccessException, IOException {
        return Server.authDAO.getAuth(authToken) == null;
    }

    private boolean invalidGameID (int gameID) throws DataAccessException {
        return Server.gameDAO.getGame(gameID) == null;
    }

    private ChessGame.TeamColor getUserColor(String username, GameData game) {
        if (username.equals(game.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        }
        if (username.equals(game.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }
        return null;
    }

}

