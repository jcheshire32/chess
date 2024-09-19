package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    //final or not...I think final?
    private ChessPosition start_position;
    private ChessPosition end_position;
    private ChessPiece.PieceType promotion_piece;

    @Override
    public String toString() {
        return "start:" + start_position +
                ", end:" + end_position +
                ", promotion:" + promotion_piece +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(start_position, chessMove.start_position) && Objects.equals(end_position, chessMove.end_position) && promotion_piece == chessMove.promotion_piece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start_position, end_position, promotion_piece);
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.start_position = startPosition;
        this.end_position = endPosition;
        this.promotion_piece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition()
    {
        return start_position;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return end_position;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotion_piece;
    }
}
