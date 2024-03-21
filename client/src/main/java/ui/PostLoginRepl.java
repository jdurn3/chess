package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.DataAccessException;
import model.GameData;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class PostLoginRepl {
    private final ServerFacade server;
    private final String serverUrl;
    private String userName;

    public PostLoginRepl(ServerFacade server, String serverUrl, String userName) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.userName = userName;
    }

    public void run() {
        System.out.println("Welcome to the Chess, " + userName + ". You are now logged in.");
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
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
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
                - create <NAME> : create a game
                - list : list games
                - join <ID> [WHITE|BLACK|<empty>] : join a game
                - observe <ID> null: observe a game
                - logout : logout when done
                - quit
                - help
                """;
    }
    private String createGame(String... params) throws DataAccessException {
        if (params.length >= 1) {
            String gameName = params[0];
            server.createGame(gameName);
            return String.format("You created the game:  %s.", gameName);
        }
        throw new DataAccessException("Expected: <Game Name>");
    }
    private String listGames() throws DataAccessException {
        var games = server.listGames();
        var result = new StringBuilder();
        for (int i = 0; i < games.length; i++) {
            GameData game = games[i];
            result.append(i + 1).append(". GameID: ").append(game.gameID())
                    .append(", White Player: ").append(game.whiteUsername())
                    .append(", Black Player: ").append(game.blackUsername())
                    .append(", Game Name: ").append(game.gameName())
                    .append('\n');
        }
        return result.toString();
    }
    private String joinGame(String... params) throws DataAccessException {
        if (params.length >= 2) {
            int gameID = Integer.parseInt(params[0]);
            String teamColor = params[1];
            ChessGame.TeamColor parsedColor;
            if (teamColor.equals("WHITE")) {
                parsedColor = ChessGame.TeamColor.WHITE;
            } else if (teamColor.equals("BLACK")) {
                parsedColor = ChessGame.TeamColor.BLACK;
            } else {
                parsedColor = null;
            }
            server.joinGame(gameID, parsedColor);
            String[] game = new String[8];
            GameRepl.main(game);
            return String.format("You successfully joined :  %s.", gameID);
        }
        throw new DataAccessException("Expected: <Game ID> [WHITE|BLACK|<empty>]");
    }
    private String observe(String... params) throws DataAccessException {
        if (params.length >= 2) {
            int gameID = Integer.parseInt(params[0]);
            server.joinGame(gameID, null);
            String[] game = new String[8];
            GameRepl.main(game);
            return String.format("You are observing game :  %s.", gameID);
        }
        throw new DataAccessException("Expected: <Game ID> null");
    }

    private String logout() throws DataAccessException {
        server.logout();
        userName = null;
        new PreLoginRepl(serverUrl).run();
        return "You have been logged out.";
    }

    private void printPrompt() {
        System.out.print(">>> ");
    }
}
