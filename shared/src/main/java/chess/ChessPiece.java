package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
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
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var validMoves = new ArrayList<ChessMove>();
        if (pieceType == ChessPiece.PieceType.KING) {
            straightLine(board, myPosition, 1, 0, validMoves); //up
            straightLine(board, myPosition, -1, 0, validMoves); //down
            straightLine(board, myPosition, 0, 1, validMoves); //right
            straightLine(board, myPosition, 0, -1, validMoves); //left
            straightLine(board, myPosition, 1, 1, validMoves); //up right
            straightLine(board, myPosition, 1, -1, validMoves); //up left
            straightLine(board, myPosition, -1, 1, validMoves); //down right
            straightLine(board, myPosition, -1, -1, validMoves); //down left
        } else if (pieceType == ChessPiece.PieceType.QUEEN) {
            straightLine(board, myPosition, 1, 0, validMoves); //up
            straightLine(board, myPosition, -1, 0, validMoves); //down
            straightLine(board, myPosition, 0, 1, validMoves); //right
            straightLine(board, myPosition, 0, -1, validMoves); //left
            straightLine(board, myPosition, 1, 1, validMoves); //up right
            straightLine(board, myPosition, 1, -1, validMoves); //up left
            straightLine(board, myPosition, -1, 1, validMoves); //down right
            straightLine(board, myPosition, -1, -1, validMoves); //down left
        } else if (pieceType == ChessPiece.PieceType.BISHOP) {
            straightLine(board, myPosition, 1, 1, validMoves); //up right
            straightLine(board, myPosition, 1, -1, validMoves); //up left
            straightLine(board, myPosition, -1, 1, validMoves); //down right
            straightLine(board, myPosition, -1, -1, validMoves); //down left
        } else if (pieceType == ChessPiece.PieceType.KNIGHT) {
            straightLine(board, myPosition, -2, -1, validMoves);
            straightLine(board, myPosition, -2, 1, validMoves);
            straightLine(board, myPosition, -1, -2, validMoves);
            straightLine(board, myPosition, -1, 2, validMoves);
            straightLine(board, myPosition, 1, 2, validMoves);
            straightLine(board, myPosition, 1, -2, validMoves);
            straightLine(board, myPosition, 2, 1, validMoves);
            straightLine(board, myPosition, 2, -1, validMoves);
        } else if (pieceType == ChessPiece.PieceType.ROOK) {
            straightLine(board, myPosition, 1, 0, validMoves); //up
            straightLine(board, myPosition, -1, 0, validMoves); //down
            straightLine(board, myPosition, 0, 1, validMoves); //right
            straightLine(board, myPosition, 0, -1, validMoves); //left
        } else if (pieceType == ChessPiece.PieceType.PAWN) {
            int infront;
            ChessPosition infront_two;
            int pawnDirection;
            int pawnLastRow;
            int startRow;
            ArrayList<ChessPosition> possibleMoves = new ArrayList<>();

            if (getTeamColor() == ChessGame.TeamColor.WHITE) {
                pawnDirection = 1;
                pawnLastRow = 8;
                startRow = 2;
            }
            else {
                pawnDirection = -1;
                pawnLastRow = 1;
                startRow = 7;
                }

            infront = myPosition.getRow() + pawnDirection;
            infront_two = new ChessPosition(infront + pawnDirection, myPosition.getColumn());
            ChessPosition infront_position = new ChessPosition(infront, myPosition.getColumn());
            ChessPosition attack_position_left = new ChessPosition(infront, myPosition.getColumn() - 1);
            ChessPosition attack_position_right = new ChessPosition(infront, myPosition.getColumn() + 1);

            //just moving forward
            if (board.getPiece(infront_position) == null) {
                if (myPosition.getRow() == startRow && board.getPiece(infront_two) == null){
                    validMoves.add(new ChessMove(myPosition, infront_two, null));
                }
                possibleMoves.add(infront_position);
            }
            // attacking
            if (isInBounds(infront, myPosition.getColumn() - 1)) {
                ChessPiece attack_left = board.getPiece(attack_position_left);
                if (attack_left != null && attack_left.getTeamColor() != this.getTeamColor()){
                    possibleMoves.add(attack_position_left);
                }
            }
            if (isInBounds(infront, myPosition.getColumn() + 1)) {
                ChessPiece attack_right = board.getPiece(attack_position_right);
                if (attack_right != null && attack_right.getTeamColor() != this.getTeamColor()){
                    possibleMoves.add(attack_position_right);
                }
            }
            for (ChessPosition move : possibleMoves){
                if (move.getRow() == pawnLastRow) {
                    validMoves.add(new ChessMove(myPosition, move, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, move, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, move, PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, move, PieceType.ROOK));
                }
                else {
                    validMoves.add(new ChessMove(myPosition,move,null));
                }
            }
        }
        return validMoves;
    }
    //accepts the NEXT position, where the piece is trying to go to
    private boolean isInBounds(int row, int col){
        return row > 0 && row < 9 && col > 0 && col < 9;
    }
    //straight line function for rook and bishop
    private void straightLine(ChessBoard board, ChessPosition myPosition, int row_direction, int col_direction, ArrayList<ChessMove> validMoves) {
        int nextRow = myPosition.getRow() + row_direction;
        int nextCol = myPosition.getColumn() + col_direction;
        //go until out of bounds or any piece
        while(isInBounds(nextRow, nextCol)){
            ChessPosition next_position = new ChessPosition(nextRow, nextCol);
            if (board.getPiece(next_position) == null) {
                validMoves.add(new ChessMove(myPosition, next_position, null));
            }
            else if (board.getPiece(next_position).getTeamColor() != this.getTeamColor()){
                validMoves.add(new ChessMove(myPosition, next_position, null));
                break;
            }
            else {
                break;
            }
            if (pieceType == ChessPiece.PieceType.KING) {
                break;
            }
            if (pieceType == PieceType.KNIGHT) {
                break;
            }
            nextRow += row_direction;
            nextCol += col_direction;
        }
    }
}