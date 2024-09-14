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
        //example:
        if (pieceType == ChessPiece.PieceType.KING) {
            
        } else if (pieceType == ChessPiece.PieceType.QUEEN) {
            
        } else if (pieceType == ChessPiece.PieceType.BISHOP) {
            
        } else if (pieceType == ChessPiece.PieceType.KNIGHT) {}
        //validMoves.add(new ChessMove(myPosition, new ChessPosition(3,2),null));
        return validMoves;
    }
}
