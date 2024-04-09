package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{

    private final int gameID;
    public Resign(CommandType commandType, int gameID, String authToken) {
        super(authToken);
        this.gameID = gameID;
    }
    public int getGameID() {
        return gameID;
    }
}
