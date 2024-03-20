package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class GameRepl {
    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 4;
    private static final String WHITE_SQUARE = EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.EMPTY;
    private static final String BLACK_SQUARE = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.EMPTY;
    private static final String EMPTY_SQUARE = EscapeSequences.SET_BG_COLOR_BLACK + "   ";

    private static final String[][] INITIAL_POSITION = {
            {EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_QUEEN,
                    EscapeSequences.BLACK_KING, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK},
            {EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
                    EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN},
            {EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
                    EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY},
            {EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
                    EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY},
            {EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
                    EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY},
            {EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
                    EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY},
            {EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
                    EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN},
            {EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_QUEEN,
                    EscapeSequences.WHITE_KING, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK}
    };

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);

        drawChessboard(out);

        out.print(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
    }

    private static void drawChessboard(PrintStream out) {
        // Draw the board in the normal orientation
        drawBoard(out, false);

        // Move the cursor to the top-left corner
        out.print(EscapeSequences.moveCursorToLocation(1, 1));

        // Draw the board in the reversed orientation
        drawBoard(out, true);
    }

    private static void drawBoard(PrintStream out, boolean reverse) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            // If reversed, start from the last row
            int currentRow = reverse ? BOARD_SIZE - row - 1 : row;
            drawRow(out, currentRow);
            if (currentRow < BOARD_SIZE - 1) {
                drawSeparator(out);
            }
        }
    }

    private static void drawRow(PrintStream out, int row) {
        for (int i = 0; i < SQUARE_SIZE; i++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean isBlackSquare = (col + row) % 2 == 0;
                if (isBlackSquare) {
                    drawSquare(out, BLACK_SQUARE);
                } else {
                    drawSquare(out, WHITE_SQUARE);
                }

                if (i == 1) {
                    drawPiece(out, row, col);
                } else {
                    drawSquare(out, EMPTY_SQUARE);
                }

                if (col < BOARD_SIZE - 1) {
                    drawVerticalLine(out);
                }
            }
            out.println();
        }
    }

    private static void drawSeparator(PrintStream out) {
        for (int i = 0; i < SQUARE_SIZE; i++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawHorizontalLine(out);
                if (col < BOARD_SIZE - 1) {
                    drawVerticalLine(out);
                }
            }
            out.println();
        }
    }

    private static void drawSquare(PrintStream out, String square) {
        out.print(square);
    }

    private static void drawHorizontalLine(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + "─".repeat(SQUARE_SIZE));
    }

    private static void drawVerticalLine(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + "│");
    }

    private static void drawPiece(PrintStream out, int row, int col) {
        String piece = INITIAL_POSITION[row][col];
        out.print(moveCursorToSquare(col, row));
        out.print(piece);
    }

    private static String moveCursorToSquare(int col, int row) {
        int x = (col * SQUARE_SIZE) + 1;
        int y = (row * SQUARE_SIZE) + 1;
        return EscapeSequences.moveCursorToLocation(x, y);
    }
}
