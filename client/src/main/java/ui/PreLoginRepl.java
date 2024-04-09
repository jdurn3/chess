package ui;

import exception.DataAccessException;
import model.UserData;
import ui.connections.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class PreLoginRepl {
    private final ServerFacade server;
    private final String serverUrl;
    private String userName = null;
    public static String authToken;

    public PreLoginRepl(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }
    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess 240. Sign in or register to start.");
        System.out.print(displayHelp());

        Scanner scanner = new Scanner(System.in);
        String inputCommand;
        do {
            printPrompt();
            inputCommand = scanner.nextLine().trim().toLowerCase();
            String result = processCommand(inputCommand);
            System.out.println(result);
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
        return """
                Available commands:
                - register <USERNAME> <PASSWORD> <EMAIL>
                - login <USERNAME> <PASSWORD>
                - quit
                """;

    }
    private String register(String...params) throws DataAccessException {
        if (params.length >= 3) {
            userName = params[0];
            UserData user = new UserData(params[0], params[1], params[2]);
            server.register(user);
            new PostLoginRepl(server, serverUrl, userName).run();
            return "";
        }
        throw new DataAccessException("Expected: <username> <password> <email>");
    }
    private String login(String... params) throws DataAccessException {
        if (params.length >= 2) {
            userName = params[0];
            server.login(userName, params[1]);
            new PostLoginRepl(server, serverUrl, userName).run();
            return "";
        }
        throw new DataAccessException("Expected: <username> <password>");
    }


    private void printPrompt() {
        System.out.print(">>> ");
    }
}
