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


        ChessPosition upper_right = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
        ChessPosition upper_left = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
        ChessPosition lower_left = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
        ChessPosition lower_right = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);

        while (inbounds(upper_right)) {
            ChessPiece newPositionPiece = board.getPiece(upper_right);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                 {
                    validMoves.add(new ChessMove(myPosition, upper_right, null));
                 }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, upper_right, null));
            }
            upper_right = new ChessPosition(upper_right.getRow()+1, upper_right.getColumn()+1);
        }
        while (inbounds(upper_left)) {
            ChessPiece newPositionPiece = board.getPiece(upper_left);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, upper_left, null));
                }
                break;
                }
            else {
                validMoves.add(new ChessMove(myPosition, upper_left, null));
            }
            upper_left = new ChessPosition(upper_left.getRow()+1, upper_left.getColumn()-1);
        }

        while (inbounds(lower_left)) {
            ChessPiece newPositionPiece = board.getPiece(lower_left);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, lower_left, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, lower_left, null));
            }
            lower_left = new ChessPosition(lower_left.getRow()-1, lower_left.getColumn()-1);
        }
        while (inbounds(lower_right)) {
            ChessPiece newPositionPiece = board.getPiece(lower_right);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, lower_right, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, lower_right, null));
            }
            lower_right = new ChessPosition(lower_right.getRow()-1, lower_right.getColumn()+1);
        }
    return validMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition front = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
        ChessPosition back = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
        ChessPosition left = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
        ChessPosition right = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);

        while (inbounds(front)) {
            ChessPiece newPositionPiece = board.getPiece(front);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, front, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, front, null));
            }
            front = new ChessPosition(front.getRow()+1, myPosition.getColumn());
        }

        while (inbounds(back)) {
            ChessPiece newPositionPiece = board.getPiece(back);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, back, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, back, null));
            }
            back = new ChessPosition(back.getRow()-1, myPosition.getColumn());
        }

        while (inbounds(left)) {
            ChessPiece newPositionPiece = board.getPiece(left);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, left, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, left, null));
            }
            left = new ChessPosition(myPosition.getRow(), left.getColumn()-1);
        }

        while (inbounds(right)) {
            ChessPiece newPositionPiece = board.getPiece(right);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, right, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, right, null));
            }
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
        ChessPosition upper_right = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
        ChessPosition upper_left = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
        ChessPosition lower_left = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
        ChessPosition lower_right = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);

        while (inbounds(upper_right)) {
            ChessPiece newPositionPiece = board.getPiece(upper_right);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, upper_right, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, upper_right, null));
            }
            upper_right = new ChessPosition(upper_right.getRow()+1, upper_right.getColumn()+1);
        }

        while (inbounds(upper_left)) {
            ChessPiece newPositionPiece = board.getPiece(upper_left);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, upper_left, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, upper_left, null));
            }
            upper_left = new ChessPosition(upper_left.getRow()+1, upper_left.getColumn()-1);
        }

        while (inbounds(lower_left)) {
            ChessPiece newPositionPiece = board.getPiece(lower_left);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, lower_left, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, lower_left, null));
            }
            lower_left = new ChessPosition(lower_left.getRow()-1, lower_left.getColumn()-1);
        }

        while (inbounds(lower_right)) {
            ChessPiece newPositionPiece = board.getPiece(lower_right);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, lower_right, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, lower_right, null));
            }
            lower_right = new ChessPosition(lower_right.getRow()-1, lower_right.getColumn()+1);
        }

        while (inbounds(front)) {
            ChessPiece newPositionPiece = board.getPiece(front);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, front, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, front, null));
            }
            front = new ChessPosition(front.getRow()+1, myPosition.getColumn());
        }

        while (inbounds(back)) {
            ChessPiece newPositionPiece = board.getPiece(back);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, back, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, back, null));
            }
            back = new ChessPosition(back.getRow()-1, myPosition.getColumn());
        }

        while (inbounds(left)) {
            ChessPiece newPositionPiece = board.getPiece(left);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, left, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, left, null));
            }
            left = new ChessPosition(myPosition.getRow(), left.getColumn()-1);
        }

        while (inbounds(right)) {
            ChessPiece newPositionPiece = board.getPiece(right);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) //opposite color
                {
                    validMoves.add(new ChessMove(myPosition, right, null));
                }
                break;
            }
            else {
                validMoves.add(new ChessMove(myPosition, right, null));
            }
            right = new ChessPosition(myPosition.getRow(), right.getColumn()+1);
        }
        return validMoves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ArrayList<ChessPosition> white_beginning = new ArrayList<>();
        ArrayList<ChessPosition> black_beginning = new ArrayList<>();
        ArrayList<ChessPosition> white_end = new ArrayList<>();
        ArrayList<ChessPosition> black_end = new ArrayList<>();


        for (int i = 0; i < 8; i++) {
            white_beginning.add(new ChessPosition(2, i));
            black_beginning.add(new ChessPosition(7, i));
            white_end.add(new ChessPosition(8, i));
            black_end.add(new ChessPosition(1, i));

        }

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition right_diagonal = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            ChessPosition left_diagonal = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            ChessPosition move_one = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());

            for (ChessPosition start : white_beginning) {
                if (Objects.equals(start, myPosition)) {
                    ChessPosition move_two = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
                    if ((board.getPiece(move_two) == null) && (board.getPiece(move_one) == null)) {
                        validMoves.add(new ChessMove(myPosition, move_two, null));
                    }
                }
            }

            boolean is_end = false;

            for (ChessPosition end : white_end) {
                if ((Objects.equals(end, move_one)) && (board.getPiece(move_one) == null)) {
                    validMoves.add(new ChessMove(myPosition, end, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, end, PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, end, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, end, PieceType.KNIGHT));
                    is_end = true;
                }
                if ((Objects.equals(end, right_diagonal)) && (inbounds(right_diagonal)) && (board.getPiece(right_diagonal) != null)) {
                    validMoves.add(new ChessMove(myPosition, right_diagonal, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, right_diagonal, PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, right_diagonal, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, right_diagonal, PieceType.KNIGHT));
                    is_end = true;
                }
                if ((Objects.equals(end, left_diagonal)) && (inbounds(left_diagonal)) && (board.getPiece(left_diagonal) != null)) {
                    validMoves.add(new ChessMove(myPosition, left_diagonal, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, left_diagonal, PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, left_diagonal, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, left_diagonal, PieceType.KNIGHT));
                    is_end = true;
                }
            }

            if (is_end) {
                return validMoves;
            }

            if (inbounds(right_diagonal) && (board.getPiece(right_diagonal) != null)) {
                if (board.getPiece(right_diagonal).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
                }
            }
            if ((inbounds(left_diagonal) && (board.getPiece(left_diagonal) != null))) {
                if (board.getPiece(left_diagonal).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                }
            }

            if ((inbounds(move_one)) && (board.getPiece(move_one) == null)) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));

            }
        }


        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPosition right_diagonal = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            ChessPosition left_diagonal = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            ChessPosition move_one = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());


            for (ChessPosition start : black_beginning) {
                if (Objects.equals(start, myPosition)) {
                    ChessPosition move_two = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                    if ((board.getPiece(move_two) == null) && (board.getPiece(move_one) == null)) {
                        validMoves.add(new ChessMove(myPosition, move_two, null));
                    }
                }
            }

            boolean is_end = false;
            for (ChessPosition end : black_end) {
                if ((Objects.equals(end, move_one)) && (board.getPiece(move_one) == null)) {
                    validMoves.add(new ChessMove(myPosition, end, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, end, PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, end, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, end, PieceType.KNIGHT));
                    is_end = true;
                }
                if ((Objects.equals(end, right_diagonal)) && (inbounds(right_diagonal)) && (board.getPiece(right_diagonal) != null)) {
                    validMoves.add(new ChessMove(myPosition, right_diagonal, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, right_diagonal, PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, right_diagonal, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, right_diagonal, PieceType.KNIGHT));
                    is_end = true;
                }
                if ((Objects.equals(end, left_diagonal)) && (inbounds(left_diagonal)) && (board.getPiece(left_diagonal) != null)) {
                    validMoves.add(new ChessMove(myPosition, left_diagonal, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, left_diagonal, PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, left_diagonal, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, left_diagonal, PieceType.KNIGHT));
                    is_end = true;
                }
            }
            if (is_end) {
                return validMoves;
            }

            if (inbounds(right_diagonal) && (board.getPiece(right_diagonal) != null)) {
                if (board.getPiece(right_diagonal).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
                }
            }

            if ((inbounds(left_diagonal) && (board.getPiece(left_diagonal) != null))) {
                if (board.getPiece(left_diagonal).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
                }
            }
            if ((inbounds(move_one)) && (board.getPiece(move_one) == null)) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
            }
        }
        return validMoves;
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


