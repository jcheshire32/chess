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
        board.resetBoard();
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
        Collection<ChessMove> goodMoves = new ArrayList<>();
        if (getBoard().getPiece(startPosition) != null){
            Collection<ChessMove> moves = getBoard().getPiece(startPosition).pieceMoves(getBoard(), startPosition);
            for (ChessMove move : moves){
                ChessPiece ogPiece = board.getPiece(move.getStartPosition());
                ChessPiece capturedPiece = board.getPiece(move.getEndPosition()); //might be null
                if (move.getPromotionPiece() != null){
                    board.addPiece(move.getEndPosition(), new ChessPiece(turn, move.getPromotionPiece()));
                } else {
                    board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                }
                //delete old position
                board.addPiece(move.getStartPosition(), null);
                if (!isInCheck(ogPiece.getTeamColor())){
                    goodMoves.add(move);
                }
                board.addPiece(move.getEndPosition(), capturedPiece);
                board.addPiece(move.getStartPosition(), ogPiece);
            }
            return goodMoves;
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
        ChessPiece ogPiece = board.getPiece(move.getStartPosition());
        if (ogPiece == null){
            throw new InvalidMoveException();
        }
        if (board.getPiece(move.getStartPosition()).getTeamColor() != turn) {
            throw new InvalidMoveException();
        }
        if (!validMoves(move.getStartPosition()).contains(move)){
            throw new InvalidMoveException();
        }
        ChessPiece capturedPiece = board.getPiece(move.getEndPosition()); //might be null
        if (move.getPromotionPiece() != null){
            board.addPiece(move.getEndPosition(), new ChessPiece(turn, move.getPromotionPiece()));
        } else {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        }
        //delete old position
        board.addPiece(move.getStartPosition(), null);
        if (isInCheck(turn)){
            board.addPiece(move.getStartPosition(), ogPiece);
            board.addPiece(move.getEndPosition(), capturedPiece);
            throw new InvalidMoveException();
        }
        if (turn == TeamColor.WHITE){
            turn = TeamColor.BLACK;
        } else {
            turn = TeamColor.WHITE;
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
        ChessPosition kingPosition = null;
        //iterate over my pieces
        for (int row=1;row<9;row++) {
            for (int col=1;col<9;col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor && currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = currentPosition;
                    break;
                }
            }
            if (kingPosition != null) {
                break;
            }
        }
        //iterate over the board and get all the opponents pieces
        ArrayList<ChessPiece> badPieces = new ArrayList<>();
        ArrayList<ChessPosition> badPositions = new ArrayList<>();
        for (int row=1;row<9;row++) {
            for (int col=1;col<9;col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() != teamColor) {
                    badPieces.add(currentPiece);
                    badPositions.add(currentPosition);
                }
            }
        }
        //iterate over all the bad guys pieces moves
        for (int i=0;i<badPieces.size();i++) {
            ChessPiece badGuy = badPieces.get(i);
            ChessPosition badGuyPosition = badPositions.get(i);

            Collection<ChessMove> badMoves = badGuy.pieceMoves(getBoard(), badGuyPosition);
            //see if in those arrays of moves, if it contains attack my king
            for (ChessMove move : badMoves) {
                if (move.getEndPosition().equals(kingPosition)){
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
        ArrayList<ChessPiece> goodPieces = new ArrayList<>();
        ArrayList<ChessPosition> goodPositions = new ArrayList<>();
        //if incheck
        if (isInCheck(teamColor)) {
            //iterate over board for MY pieces
            iterateOverBoard(teamColor, goodPieces, goodPositions);
            //iterate over moves for my pieces
            for (int i=0;i<goodPieces.size();i++) {
                ChessPiece goodGuy = goodPieces.get(i);
                ChessPosition goodGuyPosition = goodPositions.get(i);

                Collection<ChessMove> goodMoves = goodGuy.pieceMoves(getBoard(), goodGuyPosition);
                //Check if I can kill the guy attacking me or block him from killing my king
                for (ChessMove move : goodMoves) {
                    //try each move and see if I'm still in check
                    ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
                    board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                    board.addPiece(move.getStartPosition(), null);
                    //if still in check, undo and go back to the original board
                    boolean stillInCheck = isInCheck(teamColor);

                    board.addPiece(move.getStartPosition(), board.getPiece(move.getEndPosition()));
                    board.addPiece(move.getEndPosition(), capturedPiece);
                    //if it does take me out of check return false
                    if(!stillInCheck) {
                        return false;
                    }
                    //if it doesn't take me out of check, continue
                }
            }
            return true;
        }
        return false;
    }

    private void iterateOverBoard(TeamColor teamColor, ArrayList<ChessPiece> goodPieces, ArrayList<ChessPosition> goodPositions) {
        for (int row=1;row<9;row++) {
            for (int col=1;col<9;col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    goodPieces.add(currentPiece);
                    goodPositions.add(currentPosition);
                }
            }
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (turn != teamColor){
            return false;
        }
        ArrayList<ChessPiece> goodPieces = new ArrayList<>();
        ArrayList<ChessPosition> goodPositions = new ArrayList<>();
        iterateOverBoard(teamColor, goodPieces, goodPositions);
        // iterate over the moves of my pieces
        Collection<ChessMove> goodMoves = new ArrayList<>();
        for (int i=0;i<goodPieces.size();i++) {
            ChessPosition goodGuyPosition = goodPositions.get(i);

            Collection<ChessMove> goodMove = validMoves(goodGuyPosition);
            goodMoves.addAll(goodMove);
        }
        // if that list of moves is empty, and I'm not in check. Stalemate
        return goodMoves.isEmpty() && !isInCheck(teamColor); //if else statement simplified
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
}
