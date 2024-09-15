package chess;

import java.util.ArrayList;
import java.util.Collection;

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
        //make helper to check if next move is out of bounds
        if (pieceType == ChessPiece.PieceType.KING) {
            int[][] all_king_moves = {
                    {1, -1},{1, 0},{1, 1},{0, -1},{0, 1},{-1, -1}, {-1, 0}, {-1, 1}
            };
            for (int [] move : all_king_moves) {
                int next_row = myPosition.getRow() + move[0];
                int next_col = myPosition.getColumn() + move[1];
                ChessPosition next_position = new ChessPosition(next_row, next_col);
                if (isInBounds(next_row, next_col)) {
                    //if moving to empty square
                    if (board.getPiece(next_position) == null) {
                        validMoves.add(new ChessMove(myPosition, next_position, null));
                    }
                    //if moving to spot with an opponent
                    //if the color of the piece at the next position is not this piece's color
                    else if (board.getPiece(next_position).getTeamColor() != this.getTeamColor()){
                        validMoves.add(new ChessMove(myPosition, next_position, null));
                    }
                    //those are the only 2 scenarios where it's okay to move
                }
            }
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
            //does the way I did it only work for white?
            straightLine(board, myPosition, 1, 1, validMoves); //up right
            straightLine(board, myPosition, 1, -1, validMoves); //up left
            straightLine(board, myPosition, -1, 1, validMoves); //down right
            straightLine(board, myPosition, -1, -1, validMoves); //down left
        } else if (pieceType == ChessPiece.PieceType.KNIGHT) {
            int[][] all_knight_moves = {
                    {-2, -1},{-2,1},{-1,-2},{-1,2},{1,-2},{1,2},{2,-1},{2,1}
            };
            for (int [] move : all_knight_moves) {
                int next_row = myPosition.getRow() + move[0];
                int next_col = myPosition.getColumn() + move[1];
                ChessPosition next_position = new ChessPosition(next_row, next_col);
                if (isInBounds(next_row, next_col)) {
                    //if moving to empty square
                    if (board.getPiece(next_position) == null) {
                        validMoves.add(new ChessMove(myPosition, next_position, null));
                    }
                    //if moving to spot with an opponent
                    else if (board.getPiece(next_position).getTeamColor() != this.getTeamColor()){
                        validMoves.add(new ChessMove(myPosition, next_position, null));
                    }
                    //those are the only 2 scenarios where it's okay to move
                }
            }

        } else if (pieceType == ChessPiece.PieceType.ROOK) {
            straightLine(board, myPosition, 1, 0, validMoves); //up
            straightLine(board, myPosition, -1, 0, validMoves); //down
            straightLine(board, myPosition, 0, 1, validMoves); //right
            straightLine(board, myPosition, 0, -1, validMoves); //left

        } else if (pieceType == ChessPiece.PieceType.PAWN) {

        }
        //validMoves.add(new ChessMove(myPosition, new ChessPosition(3,2),null));
        return validMoves;
    }

    //pawn
    //idk how to get this one going....
    //I guess can only increase it's numbers by 1? and checking if it's attacking can be later, not sure about the first move being 2 tho...

    //accepts the NEXT position, where the piece is trying to go to
    private boolean isInBounds(int row, int col){
        if(row > 0 && row < 9 && col > 0 && col < 9){
            return true;
        }
        else {
            return false;
        }
    }
    //straight line function for rook and bishop
    //do i need more parameters?
    private void straightLine(ChessBoard board, ChessPosition myPosition, int row_direction, int col_direction, ArrayList<ChessMove> validMoves) {
        int nextRow = myPosition.getRow() + row_direction;
        int nextCol = myPosition.getColumn() + col_direction;
        //go until out of bounds or any piece
        //as TA about this function
        while(isInBounds(nextRow, nextCol)){
            ChessPosition next_position = new ChessPosition(nextRow, nextCol);
            if (board.getPiece(next_position) == null) {
                validMoves.add(new ChessMove(myPosition, next_position, null));
            }
            else if (board.getPiece(next_position).getTeamColor() != this.getTeamColor()){
                validMoves.add(new ChessMove(myPosition, next_position, null));
            }
            break;
        }
        nextRow += row_direction;
        nextCol += col_direction;
    }
}