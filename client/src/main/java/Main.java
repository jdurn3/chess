import chess.*;
import ui.PreLoginRepl;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        new PreLoginRepl("http://localhost:3000").run();
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}