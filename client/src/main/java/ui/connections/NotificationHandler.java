package ui.connections;


import chess.ChessGame;
import exception.DataAccessException;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {

    void initializeBoard(LoadGameMessage message) throws DataAccessException;

    void notificationHandler(Notification notification);

    void errorHandler(ErrorMessage errorMessage);
}
