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
    //IJ said to make these both final, I think that's right cause it can't change....
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
        if (pieceType == ChessPiece.PieceType.KING) {

        } else if (pieceType == ChessPiece.PieceType.QUEEN) {
            
        } else if (pieceType == ChessPiece.PieceType.BISHOP) {
            
        } else if (pieceType == ChessPiece.PieceType.KNIGHT) {

        } else if (pieceType == ChessPiece.PieceType.ROOK) {

        } else if (pieceType == ChessPiece.PieceType.PAWN) {

        }
        //validMoves.add(new ChessMove(myPosition, new ChessPosition(3,2),null));
        return validMoves;
    }

    //king
    //current position increase either number or both numbers by 1
    private void addKingMoves(ChessBoard board, ChessPosition myPosition) {

    }
    //queen - this part doesn't worry if it's blocked?
    //change first number only, or change 2nd number only, or change first and second numbers to the same degree
    //can just combine rook and bishop
    private void addQueenMoves(ChessBoard board, ChessPosition myPosition) {

    }
    //bishop
    //both numbers MUST change, and to the same degree
    private void addBishopMoves(ChessBoard board, ChessPosition myPosition) {

    }
    //knight
    //both numbers have to change, one number MUST be 1, the other MUST be 2.
    private void addKnightMoves(ChessBoard board, ChessPosition myPosition) {

    }
    //rook
    //change first number OR change second number
    private void addRookMoves(ChessBoard board, ChessPosition myPosition) {

    }
    //pawn
    //idk how to get this one going....
    //I guess can only increase it's numbers by 1? and checking if it's attacking can be later, not sure about the first move being 2 tho...
    private void addPawnMoves(ChessBoard board, ChessPosition myPosition) {

    }
}