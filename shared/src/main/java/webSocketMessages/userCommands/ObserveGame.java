package webSocketMessages.userCommands;

import chess.ChessGame;

public class ObserveGame extends UserGameCommand {
    private final int gameID;


    public ObserveGame(CommandType commandType, int gameID, String authToken) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

}
