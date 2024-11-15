package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;

import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class Board {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final ChessPiece EMPTY = null;
    private static final ChessPiece b_pawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
    private static final ChessPiece w_pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
    private static final ChessPiece b_rook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    private static final ChessPiece w_rook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    private static final ChessPiece b_knight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
    private static final ChessPiece w_knight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
    private static final ChessPiece b_bishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
    private static final ChessPiece w_bishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
    private static final ChessPiece b_queen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
    private static final ChessPiece w_queen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
    private static final ChessPiece b_king = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
    private static final ChessPiece w_king = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);


    private static final ChessPiece[][] boardStart = {
            {b_rook, b_knight, b_bishop, b_queen, b_king, b_bishop, b_knight, b_rook},
            {b_pawn, b_pawn, b_pawn, b_pawn, b_pawn, b_pawn, b_pawn, b_pawn},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {w_pawn, w_pawn, w_pawn, w_pawn, w_pawn, w_pawn, w_pawn, w_pawn},
            {w_rook, w_knight, w_bishop, w_queen, w_king, w_bishop, w_knight, w_rook}
    };

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawTicTacToeBoard(out);

        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {

        setGray(out);

        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        out.print("  ");
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(" ".repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;

        out.print(" ".repeat(prefixLength));
        printHeaderText(out, headerText);
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setGray(out);

    }

    private static void drawTicTacToeBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, boardRow);
        }
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2){
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(SET_TEXT_COLOR_BLACK);
                out.print(boardRow+1);
                out.print(" ");
            } else {
                out.print("  ");
            }
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if ((boardCol + boardRow) % 2 == 0) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(" ".repeat(prefixLength));
                    printPlayer(out, boardStart[boardRow][boardCol]);
                    out.print(" ".repeat(suffixLength));
                } else {
                    out.print(" ".repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }
                setGray(out);
            }
            if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2){
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(" ");
                out.print(SET_TEXT_COLOR_BLACK);
                out.print(boardRow+1);
            } else {
                out.print("  ");
            }
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void printPlayer(PrintStream out, ChessPiece player) {
        //translate piecetype to letter. map or switch case
        if (player == null){
            out.print(" ");
            return;
        } else if (player.getTeamColor() == ChessGame.TeamColor.BLACK) {
            out.print(SET_TEXT_COLOR_GREEN);
        } else {
            out.print(SET_TEXT_COLOR_RED);
        }
        switch (player.getPieceType()) {
            case KING:
                out.print("K");
                break;
            case QUEEN:
                out.print("Q");
                break;
            case BISHOP:
                out.print("B");
                break;
            case KNIGHT:
                out.print("N");
                break;
            case PAWN:
                out.print("P");
                break;
            case ROOK:
                out.print("R");
                break;
            default:
                break;
        }
    }
}