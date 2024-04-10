package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.DataAccessException;
import ui.connections.NotificationHandler;
import ui.connections.ServerFacade;
import ui.connections.WebSocketFacade;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;

import javax.xml.crypto.Data;
import java.util.*;

public class GameRepl implements NotificationHandler {

    private final ServerFacade server;

    private final String serverUrl;
    private final ChessGame.TeamColor playerColor;
    private final String userName;

    private ChessGame game;
    private final NotificationHandler notificationHandler = this;

    private WebSocketFacade ws;
    private final Integer gameID;
    private final String action;

    public GameRepl(ServerFacade server, String serverUrl, String userName, String action, int gameID, ChessGame.TeamColor playerColor) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.userName = userName;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.action = action;
    }
    public void run() throws DataAccessException {

        if (Objects.equals(action, "JOIN")) {
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.joinGame(gameID, playerColor);
        }

        if (Objects.equals(action, "OBSERVE")) {
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.observeGame(gameID);
        }
        
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


    public void notificationHandler(Notification notification) {
        System.out.println(notification.getNotification());
        printPrompt();
    }

    public void errorHandler(ErrorMessage errorMessage) {
        System.out.println(errorMessage.getError());
        printPrompt();
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
        initializeBoard(new LoadGameMessage(game));
        return "Redrawing chess board...";
    }

    private String leaveGame() throws DataAccessException {
        ws.leave(gameID);
        //PostLoginRepl.run();
        return "Leaving game...";
    }

    private String makeMove(String[] params) throws DataAccessException {
        if (params.length >= 2) {
            String start = params[1];
            int [] startCoordinates = parseCoordinate(start);
            String end = params[2];
            int [] endCoordinates = parseCoordinate(end);
           // need to put in all the error catching statements for if any of the inputs don't match
            ChessPosition startPosition = new ChessPosition(startCoordinates[0], startCoordinates[1]);
            ChessPosition endPosition = new ChessPosition(endCoordinates[0], endCoordinates[1]);
            ChessMove move = new ChessMove(startPosition, endPosition, null);
            ws.makeMove(gameID, move);
        }
        return "Making move: " + String.join(" ", params);
    }

    private String resign() throws DataAccessException {
        ws.resign(gameID);
        return "Resigning from the game...";
    }

    private String highlightLegalMoves(String[] params) throws DataAccessException {
//            String startPosition = params[1];
//            int[] coordinates = parseCoordinate(startPosition);
//            ChessPosition position = new ChessPosition(coordinates[0], coordinates[1]);
//            Collection<ChessMove> validMoves = game.validMoves(position);
//
//            // Print the board with highlighted valid moves
//            String[][] highlightedBoard = initializeBoard();
//            highlightedBoard[coordinates[0]][coordinates[1]] = "\u001B[43m" + " " + highlightedBoard[coordinates[0]][coordinates[1]] + " "; // Yellow highlight for selected square
//
//            for (ChessMove move : validMoves) {
//                int[] endCoordinates = {move.getEndPosition().getRow(), move.getEndPosition().getColumn()};
//                highlightedBoard[endCoordinates[0]][endCoordinates[1]] = "\u001B[42m" + " " + highlightedBoard[endCoordinates[0]][endCoordinates[1]] + " "; // Green highlight for valid moves
//            }
//
//            printBoard(highlightedBoard);
        return "Highlighting legal moves for piece: ";
    }


    @Override
    public void initializeBoard(LoadGameMessage message) throws DataAccessException {
        String[][] board = new String[8][8];
        game = message.getGame();

        // Initialize the board with empty squares
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = " ";
            }
        }

        // Populate the board with pieces from the game
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = game.getBoard().getPiece(new ChessPosition(i+1, j+1));
                if (piece != null) {
                    String pieceSymbol = getPieceSymbol(piece);
                    board[i][j] = pieceSymbol;
                }
            }
        }

        // Determine whether to print the board in regular or reverse orientation based on player's color
        if (playerColor == ChessGame.TeamColor.WHITE || playerColor == null) {
            printBoardReverse(board);
        } else {
            printBoard(board);
        }
    }

    private String getPieceSymbol(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "K" : "k";
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "Q" : "q";
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "B" : "b";
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "N" : "n";
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "R" : "r";
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "P" : "p";
            default -> " "; // Empty square
        };
    }

    // Function to print the board in regular orientation
    public static void printBoard(String[][] board) {
        boolean isWhiteSquare = true;
        System.out.println("  A  B  C  D  E  F  G  H");
        for (int i = 0; i < 8; i++) {
            System.out.print((char) ('1' + i) + " ");
            for (int j = 0; j < 8; j++) {
                isWhiteSquare = isWhiteSquare(board, isWhiteSquare, i, j);
            }
            isWhiteSquare = !isWhiteSquare;
            System.out.println("\u001B[0m"); // Reset colors
        }
    }

    private static boolean isWhiteSquare(String[][] board, boolean isWhiteSquare, int i, int j) {
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
        return isWhiteSquare;
    }

    // Function to print the board in reverse orientation
    public static void printBoardReverse(String[][] board) {
        boolean isWhiteSquare = true;
        System.out.println("  H  G  F  E  D  C  B  A");
        for (int i = 7; i >= 0; i--) {
            System.out.print((char) ('1' + i) + " ");
            for (int j = 7; j >= 0; j--) {
                isWhiteSquare = isWhiteSquare(board, isWhiteSquare, i, j);
            }
            isWhiteSquare = !isWhiteSquare;
            System.out.println("\u001B[0m"); // Reset colors
        }
    }

    public static int[] parseCoordinate(String coordinate) throws DataAccessException {
        if (coordinate.length() != 2) {
            throw new DataAccessException("Invalid coordinate format: " + coordinate);
        }

        char column = Character.toUpperCase(coordinate.charAt(0));
        int row = Character.getNumericValue(coordinate.charAt(1));

        if (column < 'A' || column > 'H' || row < 1 || row > 8) {
            throw new DataAccessException("Invalid coordinate format: " + coordinate);
        }

        int columnIndex = column - 'A'; // Convert column letter to zero-based index
        int rowIndex = 8 - row; // Convert row number to zero-based index

        return new int[]{rowIndex, columnIndex};
    }


}
