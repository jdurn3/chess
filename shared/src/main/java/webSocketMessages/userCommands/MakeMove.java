package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private final int gameID;
    private final chess.ChessMove move;
    public MakeMove(CommandType commandType, int gameID, ChessMove move, String authToken) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }

    public int getGameID() {
        return gameID;
    }
}
