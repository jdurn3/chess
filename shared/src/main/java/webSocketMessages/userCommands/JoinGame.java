package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinGame extends UserGameCommand {
    private final int gameID;
    private final ChessGame.TeamColor playerColor;

    public JoinGame(CommandType commandType, int gameID, ChessGame.TeamColor playerColor, String authToken) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

}
