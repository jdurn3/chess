package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor team;
    private ChessBoard board;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    public ChessBoard copyChessboard(ChessBoard board) {
        ChessPiece[][] copiedBoard = new ChessPiece[8][8];
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                if (board.getPiece(position) != null) {
                    copiedBoard[row][col] = new ChessPiece(board.getPiece(position).getTeamColor(), board.getPiece(position).getPieceType());
                } else {
                    copiedBoard[row][col] = null;
                }
            }
        }
        return new ChessBoard(copiedBoard);
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        TeamColor team = getTeamTurn();
        if ((piece != null) && (piece.getTeamColor() == team)) {
            if (isInCheck(team)) {

            }
            Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
            for (ChessMove move: possibleMoves) {
                makeMove(move);

            }
        }
    }

    public ChessPosition findKing(TeamColor teamColor) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if ((piece.getPieceType() == ChessPiece.PieceType.KING) && (piece.getTeamColor() == teamColor)) {
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */



    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> otherTeamMoves = new HashSet<>();
        ChessPosition kingPosition = findKing(team);
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if ((piece != null) && (piece.getTeamColor() != teamColor)) {
                    otherTeamMoves.addAll(piece.pieceMoves(board, position));
                }
            }
        }
        for (ChessMove move : otherTeamMoves) {
            if (kingPosition == move.getEndPosition()){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
