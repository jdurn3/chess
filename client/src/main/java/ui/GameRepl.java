package ui;

public class GameRepl {
    public static void main(String[] args) {
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

        printBoard(board);

        printBoardReverse(board);
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
