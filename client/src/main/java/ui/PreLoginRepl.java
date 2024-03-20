package ui;

import exception.DataAccessException;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class PreLoginRepl {
    private final ServerFacade server;
    private final String serverUrl;
    private String userName = null;
    private State state = State.SIGNEDOUT;

    public PreLoginRepl(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }
    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the Chess Client. Sign in or register to start.");
        System.out.print(displayHelp());

        Scanner scanner = new Scanner(System.in);
        String inputCommand;
        do {
            printPrompt();
            inputCommand = scanner.nextLine().trim().toLowerCase();
            processCommand(inputCommand);
        } while (!inputCommand.equals("quit"));
    }

    public String processCommand(String input) {
            try {
                var tokens = input.toLowerCase().split(" ");
                var cmd = (tokens.length > 0) ? tokens[0] : "help";
                var params = Arrays.copyOfRange(tokens, 1, tokens.length);
                return switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> "quit";
                    default -> displayHelp();
                };
            } catch (DataAccessException ex) {
                return ex.getMessage();
            }
        }

    private String displayHelp() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <USERNAME>
                    - quit
                    """;
        }
        return """
                Available commands:
                - register <USERNAME> <PASSWORD> <EMAIL>
                - login <USERNAME> <PASSWORD>
                - quit
                """;

    }
    private String register(String...params) throws DataAccessException {
        if (params.length >= 3) {
            state = State.SIGNEDIN;
            userName = params[0];
            UserData user = new UserData(params[0], params[1], params[2]);
            server.register(user);
            new PostLoginRepl(server, serverUrl, userName);
            return String.format("You registered as %s.", userName);
        }
        throw new DataAccessException("Expected: <username> <password> <email>");
    }
    private String login(String... params) throws DataAccessException {
        if (params.length >= 2) {
            state = State.SIGNEDIN;
            userName = params[0];
            server.login(userName, params[1]);
            new PostLoginRepl(server, serverUrl, userName);
            return String.format("You signed in as %s.", userName);
        }
        throw new DataAccessException("Expected: <username> <password>");
    }


    private void printPrompt() {
        System.out.print(">>> ");
    }
}
