package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    private final int gameID;
    public Leave(CommandType commandType, int gameID, String authToken) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = commandType;
    }

    public int getGameID() {
        return gameID;
    }
}
