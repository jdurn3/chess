package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{

    private final String errorMessage;
    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;

    }

    public String getError() {
        return errorMessage;
    }
}
