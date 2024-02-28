package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            return rookMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            return knightMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            return kingMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
            return queenMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            return pawnMoves(board, myPosition);
        }
        return null;
    }

    private boolean inbounds(ChessPosition myPosition) {
       boolean b1 =  (myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8);
       boolean b2 = (myPosition.getRow() <= 8 && myPosition.getRow() >= 1);
       return b1 && b2;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();


        duplicate(board, myPosition, validMoves);
        return validMoves;
    }

    private void duplicate(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves) {
        ChessPosition upperRight = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
        ChessPosition upperLeft = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
        ChessPosition lowerLeft = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
        ChessPosition lowerRight = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);

        while (inbounds(upperRight)) {
            if (duplicate2(board, myPosition, validMoves, upperRight)) break;
            upperRight = new ChessPosition(upperRight.getRow()+1, upperRight.getColumn()+1);
        }
        while (inbounds(upperLeft)) {
            if (duplicate2(board, myPosition, validMoves, upperLeft)) break;
            upperLeft = new ChessPosition(upperLeft.getRow()+1, upperLeft.getColumn()-1);
        }

        while (inbounds(lowerLeft)) {
            if (duplicate2(board, myPosition, validMoves, lowerLeft)) break;
            lowerLeft = new ChessPosition(lowerLeft.getRow()-1, lowerLeft.getColumn()-1);
        }
        while (inbounds(lowerRight)) {
            if (duplicate2(board, myPosition, validMoves, lowerRight)) break;
            lowerRight = new ChessPosition(lowerRight.getRow()-1, lowerRight.getColumn()+1);
        }
    }

    private boolean duplicate2(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, ChessPosition upperRight) {
        ChessPiece newPositionPiece = board.getPiece(upperRight);
        if (newPositionPiece != null) {
            if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
             {
                validMoves.add(new ChessMove(myPosition, upperRight, null));
             }
            return true;
        }
        else {
            validMoves.add(new ChessMove(myPosition, upperRight, null));
        }
        return false;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition front = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
        ChessPosition back = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
        ChessPosition left = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
        ChessPosition right = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);

        while (inbounds(front)) {
            if (duplicate2(board, myPosition, validMoves, front)) break;
            front = new ChessPosition(front.getRow()+1, myPosition.getColumn());
        }

        while (inbounds(back)) {
            if (duplicate2(board, myPosition, validMoves, back)) break;
            back = new ChessPosition(back.getRow()-1, myPosition.getColumn());
        }

        while (inbounds(left)) {
            if (duplicate2(board, myPosition, validMoves, left)) break;
            left = new ChessPosition(myPosition.getRow(), left.getColumn()-1);
        }

        while (inbounds(right)) {
            if (duplicate2(board, myPosition, validMoves, right)) break;
            right = new ChessPosition(myPosition.getRow(), right.getColumn()+1);
        }

        return validMoves;

    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ArrayList<ChessPosition> possiblePositions = new ArrayList<>();

        possiblePositions.add(new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-2));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+2,myPosition.getColumn()-1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+2,myPosition.getColumn()+1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+2));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+2));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-2,myPosition.getColumn()+1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-2,myPosition.getColumn()-1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()-2));

        for (ChessPosition position : possiblePositions) {
            if (inbounds(position)) {
                if (board.getPiece(position) != null) {
                    if (board.getPiece(position).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        continue;
                    }
                }
                validMoves.add(new ChessMove(myPosition, position, null));
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ArrayList<ChessPosition> possiblePositions = new ArrayList<>();

        possiblePositions.add(new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()+1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()-1));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()-1));

        for (ChessPosition position : possiblePositions) {
            if (inbounds(position)) {
                if ((board.getPiece(position) != null) && (board.getPiece(position).getTeamColor() == board.getPiece(myPosition).getTeamColor())) {
                    continue;
                }
                validMoves.add(new ChessMove(myPosition, position, null));
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition front = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
        ChessPosition back = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
        ChessPosition left = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
        ChessPosition right = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);
        duplicate(board, myPosition, validMoves);

        while (inbounds(front)) {
            if (duplicate2(board, myPosition, validMoves, front)) break;
            front = new ChessPosition(front.getRow()+1, myPosition.getColumn());
        }

        while (inbounds(back)) {
            if (duplicate2(board, myPosition, validMoves, back)) break;
            back = new ChessPosition(back.getRow()-1, myPosition.getColumn());
        }

        while (inbounds(left)) {
            if (duplicate2(board, myPosition, validMoves, left)) break;
            left = new ChessPosition(myPosition.getRow(), left.getColumn()-1);
        }

        while (inbounds(right)) {
            if (duplicate2(board, myPosition, validMoves, right)) break;
            right = new ChessPosition(myPosition.getRow(), right.getColumn()+1);
        }
        return validMoves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ArrayList<ChessPosition> whiteBeginning = new ArrayList<>();
        ArrayList<ChessPosition> blackBeginning = new ArrayList<>();
        ArrayList<ChessPosition> whiteEnd = new ArrayList<>();
        ArrayList<ChessPosition> blackEnd = new ArrayList<>();


        for (int i = 0; i < 8; i++) {
            whiteBeginning.add(new ChessPosition(2, i));
            blackBeginning.add(new ChessPosition(7, i));
            whiteEnd.add(new ChessPosition(8, i));
            blackEnd.add(new ChessPosition(1, i));

        }

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition rightDiagonal = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            ChessPosition leftDiagonal = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            ChessPosition moveOne = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());

            for (ChessPosition start : whiteBeginning) {
                if (Objects.equals(start, myPosition)) {
                    ChessPosition moveTwo = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
                    if ((board.getPiece(moveTwo) == null) && (board.getPiece(moveOne) == null)) {
                        validMoves.add(new ChessMove(myPosition, moveTwo, null));
                    }
                }
            }

            if (duplicate3(board, myPosition, validMoves, whiteEnd, rightDiagonal, leftDiagonal, moveOne))
                return validMoves;

            if (inbounds(rightDiagonal) && (board.getPiece(rightDiagonal) != null)) {
                if (board.getPiece(rightDiagonal).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
                }
            }
            if ((inbounds(leftDiagonal) && (board.getPiece(leftDiagonal) != null))) {
                if (board.getPiece(leftDiagonal).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                }
            }

            if ((inbounds(moveOne)) && (board.getPiece(moveOne) == null)) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));

            }
        }


        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPosition rightDiagonal = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            ChessPosition leftDiagonal = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            ChessPosition moveOne = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());


            for (ChessPosition start : blackBeginning) {
                if (Objects.equals(start, myPosition)) {
                    ChessPosition moveTwo = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                    if ((board.getPiece(moveTwo) == null) && (board.getPiece(moveOne) == null)) {
                        validMoves.add(new ChessMove(myPosition, moveTwo, null));
                    }
                }
            }

            if (duplicate3(board, myPosition, validMoves, blackEnd, rightDiagonal, leftDiagonal, moveOne))
                return validMoves;

            if (inbounds(rightDiagonal) && (board.getPiece(rightDiagonal) != null)) {
                if (board.getPiece(rightDiagonal).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
                }
            }

            if ((inbounds(leftDiagonal) && (board.getPiece(leftDiagonal) != null))) {
                if (board.getPiece(leftDiagonal).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
                }
            }
            if ((inbounds(moveOne)) && (board.getPiece(moveOne) == null)) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
            }
        }
        return validMoves;
    }

    private boolean duplicate3(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, ArrayList<ChessPosition> blackEnd, ChessPosition rightDiagonal, ChessPosition leftDiagonal, ChessPosition moveOne) {
        boolean isEnd = false;
        for (ChessPosition end : blackEnd) {
            if ((Objects.equals(end, moveOne)) && (board.getPiece(moveOne) == null)) {
                validMoves.add(new ChessMove(myPosition, end, PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, end, PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, end, PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, end, PieceType.KNIGHT));
                isEnd = true;
            }
            if ((Objects.equals(end, rightDiagonal)) && (inbounds(rightDiagonal)) && (board.getPiece(rightDiagonal) != null)) {
                validMoves.add(new ChessMove(myPosition, rightDiagonal, PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, rightDiagonal, PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, rightDiagonal, PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, rightDiagonal, PieceType.KNIGHT));
                isEnd = true;
            }
            if ((Objects.equals(end, leftDiagonal)) && (inbounds(leftDiagonal)) && (board.getPiece(leftDiagonal) != null)) {
                validMoves.add(new ChessMove(myPosition, leftDiagonal, PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, leftDiagonal, PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, leftDiagonal, PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, leftDiagonal, PieceType.KNIGHT));
                isEnd = true;
            }
        }
        if (isEnd) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}


