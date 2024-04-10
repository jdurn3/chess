package ui.connections;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.DataAccessException;
import ui.PreLoginRepl;
import ui.connections.NotificationHandler;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(serverMessage);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame(int gameID, ChessGame.TeamColor playerColor) throws DataAccessException {
            try {
                JoinGame joinAction = new JoinGame(UserGameCommand.CommandType.JOIN_PLAYER, gameID, playerColor, PreLoginRepl.authToken);
                String jsonAction = new Gson().toJson(joinAction);
                session.getBasicRemote().sendText(jsonAction);
            } catch (IOException ex) {
                throw new DataAccessException(ex.getMessage());
            }
    }

    public void observeGame(int gameID) throws DataAccessException {
        try {
            ObserveGame observeAction = new ObserveGame(UserGameCommand.CommandType.JOIN_OBSERVER, gameID, PreLoginRepl.authToken);
            String jsonAction = new Gson().toJson(observeAction);
            session.getBasicRemote().sendText(jsonAction);
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void makeMove(int gameID, ChessMove move) throws DataAccessException {
        try {
            MakeMove makeMove = new MakeMove(UserGameCommand.CommandType.MAKE_MOVE, gameID, move, PreLoginRepl.authToken);
            String jsonAction = new Gson().toJson(makeMove);
            session.getBasicRemote().sendText(jsonAction);
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void leave(int gameID) throws DataAccessException {
        try {
            Leave leaveAction = new Leave(UserGameCommand.CommandType.LEAVE, gameID, PreLoginRepl.authToken);
            String jsonAction = new Gson().toJson(leaveAction);
            session.getBasicRemote().sendText(jsonAction);
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void resign(int gameID) throws DataAccessException {
        try {
            Resign resign = new Resign(UserGameCommand.CommandType.RESIGN, gameID, PreLoginRepl.authToken);
            String jsonAction = new Gson().toJson(resign);
            session.getBasicRemote().sendText(jsonAction);
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

}