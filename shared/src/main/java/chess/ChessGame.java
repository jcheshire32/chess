package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private TeamColor turn = TeamColor.WHITE;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
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
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (getBoard().getPiece(startPosition) != null){
            return getBoard().getPiece(startPosition).pieceMoves(getBoard(), startPosition);
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
        //lot more to do still.... including switching turns
        if (validMoves(move.getStartPosition()).contains(move)){
            //add piece at new position
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            //delete old position
            board.addPiece(move.getStartPosition(), null);
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //get king position
        ChessPosition kingPosition;
        for (int row=1;row<9;row++) {
            for (int col=1;col<9;col++) {
                ChessPosition current_position = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(current_position);
                if (currentPiece != null && currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = current_position;
                    break;
                }
            }
        }
        //iterate over the board and get all the opponents pieces
        ArrayList<ChessPiece> bad_pieces = new ArrayList<>();
        ArrayList<ChessPosition> bad_positions = new ArrayList<>();
        for (int row=1;row<9;row++) {
            for (int col=1;col<9;col++) {
                ChessPosition current_position = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(current_position);
                if (currentPiece != null && currentPiece.getTeamColor() != teamColor) {
                    bad_pieces.add(currentPiece);
                    bad_positions.add(current_position);
                }
            }
        }
        //iterate over all the bad guys pieces moves
        for (int i=0;i<bad_pieces.size();i++) {
            ChessPiece bad_guy = bad_pieces.get(i);
            ChessPosition bad_guy_position = bad_positions.get(i);

            Collection<ChessMove> bad_moves = bad_guy.pieceMoves(getBoard(), bad_guy_position);
            //see if in those arrays of moves, if it contains attack my king
            for (ChessMove move : bad_moves) {
                if (move.getEndPosition() == kingPosition){
                    return true;
                }
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
        ArrayList<ChessPiece> good_pieces = new ArrayList<>();
        ArrayList<ChessPosition> good_positions = new ArrayList<>();
        //if incheck
        if (isInCheck(teamColor)) {
            //iterate over board for MY pieces
            for (int row=1;row<9;row++) {
                for (int col=1;col<9;col++) {
                    ChessPosition current_position = new ChessPosition(row, col);
                    ChessPiece currentPiece = board.getPiece(current_position);
                    if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                        good_pieces.add(currentPiece);
                        good_positions.add(current_position);
                    }
                }
            }
            //iterate over moves for my pieces
            for (int i=0;i<good_pieces.size();i++) {
                ChessPiece good_guy = good_pieces.get(i);
                ChessPosition good_guy_position = good_positions.get(i);

                Collection<ChessMove> good_moves = good_guy.pieceMoves(getBoard(), good_guy_position);

                for (ChessMove move : good_moves) {
                    //See if they can kill the guy attacking me or block him from killing my king
                    //aka try each move and see if I'm still in check
                    ChessPiece captured_piece = board.getPiece(move.getEndPosition());
                    board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                    board.addPiece(move.getStartPosition(), null);

                }
            }
            //see if after executing a move if that takes me out of check
            //if it does take me out of check return false
            //if it doesn't take me out of check, continue
            //return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // iterate over board for my pieces
        // iterate over the moves of my pieces
        // if that list of moves is empty and I'm not in check. Stalemate
        // if validMoves.length == 0 && isInCheck == false;
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
        return board;
    }
    //make a function for iterating over all the moves team x can make. parameter is the team. return is validMoves;
}
