package ui;

import exception.DataAccessException;
import ui.connections.NotificationHandler;
import ui.connections.ServerFacade;
import ui.connections.WebSocketFacade;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.Scanner;

public class GameRepl {

    private final ServerFacade server;

    private final String serverUrl;
    private String userName;

    private NotificationHandler notificationHandler;

    private ui.connections.WebSocketFacade ws;

    private Integer gameID;

    public GameRepl(ServerFacade server, String serverUrl, String userName, NotificationHandler notificationHandler, WebSocketFacade ws, int gameID) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.userName = userName;
        this.notificationHandler = notificationHandler;
        this.ws = ws;
        this.gameID = gameID;
    }
    public void run() {

        String[][] board = initializeBoard();

        printBoard(board);

        printBoardReverse(board);
        
        
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

    private String[][] initializeBoard() {
        String[][] board = new String[8][8];
        boolean isWhiteSquare = true;

        // Initialize the board with empty squares
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = " ";
            }
        }

        // Set up white pieces
        board[0][0] = "R";
        board[0][1] = "N";
        board[0][2] = "B";
        board[0][3] = "Q";
        board[0][4] = "K";
        board[0][5] = "B";
        board[0][6] = "N";
        board[0][7] = "R";
        for (int i = 0; i < 8; i++) {
            board[1][i] = "P";
        }

        // Set up black pieces
        board[7][0] = "r";
        board[7][1] = "n";
        board[7][2] = "b";
        board[7][3] = "q";
        board[7][4] = "k";
        board[7][5] = "b";
        board[7][6] = "n";
        board[7][7] = "r";
        for (int i = 0; i < 8; i++) {
            board[6][i] = "p";
        }
        return board;
    }

    private void printPrompt() {
        System.out.print(">>> ");
    }
    public String processCommand(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> displayHelp();
                case "redraw" -> redrawChessBoard();
                case "leave" -> leaveGame();
                case "make" -> makeMove(params);
                case "resign" -> resign();
                case "highlight" -> highlightLegalMoves(params);
                default -> "Invalid command. Type 'help' for available commands.";
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    private String displayHelp() {
        return """
            Available commands:
            - help
            - redraw : Redraws the chess board.
            - leave : Remove yourself from the game.
            - make <move>
            - resign
            - highlight <piece>
            """;
    }

    private String redrawChessBoard() throws DataAccessException {
        // Implementation for redrawing the chess board
        return "Redrawing chess board...";
    }

    private String leaveGame() throws DataAccessException {
        ws.leave(gameID);
        return "Leaving game...";
    }

    private String makeMove(String[] params) throws DataAccessException {
        // Implementation for making a move
        return "Making move: " + String.join(" ", params);
    }

    private String resign() throws DataAccessException {
        ws.resign(gameID);
        return "Resigning from the game...";
    }

    private String highlightLegalMoves(String[] params) throws DataAccessException {
        // Implementation for highlighting legal moves
        return "Highlighting legal moves for piece: " + String.join(" ", params);
    }

    // Function to print the board in regular orientation
    public static void printBoard(String[][] board) {
        boolean isWhiteSquare = true;
        System.out.println("  A  B  C  D  E  F  G  H");
        for (int i = 0; i < 8; i++) {
            System.out.print((char) ('1' + i) + " ");
            for (int j = 0; j < 8; j++) {
                if (isWhiteSquare) {
                    System.out.print("\u001B[47m"); // White background
                } else {
                    System.out.print("\u001B[100m"); // Grey background
                }

                if (Character.isUpperCase(board[i][j].charAt(0))) {
                    System.out.print("\u001B[34m" + " " + board[i][j] + " "); // Blue for white pieces
                } else {
                    System.out.print("\u001B[31m" + " " + board[i][j] + " "); // Red for black pieces
                }

                isWhiteSquare = !isWhiteSquare;
            }
            isWhiteSquare = !isWhiteSquare;
            System.out.println("\u001B[0m"); // Reset colors
        }
    }

    // Function to print the board in reverse orientation
    public static void printBoardReverse(String[][] board) {
        boolean isWhiteSquare = true;
        System.out.println("  H  G  F  E  D  C  B  A");
        for (int i = 7; i >= 0; i--) {
            System.out.print((char) ('1' + i) + " ");
            for (int j = 7; j >= 0; j--) {
                if (isWhiteSquare) {
                    System.out.print("\u001B[47m"); // White background
                } else {
                    System.out.print("\u001B[100m"); // Grey background
                }

                if (Character.isUpperCase(board[i][j].charAt(0))) {
                    System.out.print("\u001B[34m" + " " + board[i][j] + " "); // Blue for white pieces
                } else {
                    System.out.print("\u001B[31m" + " " + board[i][j] + " "); // Red for black pieces
                }

                isWhiteSquare = !isWhiteSquare;
            }
            isWhiteSquare = !isWhiteSquare;
            System.out.println("\u001B[0m"); // Reset colors
        }
    }
}
