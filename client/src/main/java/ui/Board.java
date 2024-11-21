package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;

import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

//loop through a board and get the pieces and print them as they go.
public class Board {

    // Board dimensions.
    private static final int BOARD_SIZE = 8;
    private ChessPiece[][] board;
//    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
//    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    public Board() {
        this.board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
    }

    public void updateBoard(ChessPiece[][] newBoard) {
        this.board = newBoard;
    }

    public void displayWhiteBoard() {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawWhiteHeaders(out);

        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            drawWhiteRow(out, i);
        }
        drawWhiteHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public void displayBlackBoard() {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawBlackHeaders(out);

        for (int i = 0; i < BOARD_SIZE; i++) {
            drawBlackRow(out, i);
        }
        drawBlackHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawWhiteHeaders(PrintStream out) {

        setGray(out);
        out.print(SET_TEXT_COLOR_BLACK);

        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        out.print("  ");
        for (String header : headers) {
            out.print("  " + header); //might need to pad with spaces again
        }
        out.println();
    }

    private static void drawBlackHeaders(PrintStream out) {
        setGray(out);
        out.print(SET_TEXT_COLOR_BLACK);
        String[] headers = {"h", "g", "f", "e", "d", "c", "b", "a"};
        out.print("  ");
        for (String header : headers) {
            out.print("  " + header);
        }
        out.println();
    }

    private void drawWhiteRow(PrintStream out, int row) {
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" " + (row + 1) + " ");
        out.print(SET_BG_COLOR_LIGHT_GREY);
        for (int col = 0; col < BOARD_SIZE; col++) {
            extracted(out, row, col);
        }
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" " + (row + 1));
        out.println();
        //might need to fix display here again.
    }

    private void extracted(PrintStream out, int row, int col) {
        if ((row + col) % 2 == 0) {
            setBlack(out);
        } else {
            setWhite(out);
        }
        printSquare(out, board[row][col]);
        setGray(out);
    }

    private void drawBlackRow(PrintStream out, int row) {
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" " + (row + 1) + " ");
        out.print(SET_BG_COLOR_LIGHT_GREY);
        for (int col = BOARD_SIZE - 1; col >= 0; col--) {
            extracted(out, row, col); //hope this fixes code quality
        }
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" " + (row + 1));
        out.println();
    }

    private void printSquare(PrintStream out, ChessPiece piece) { //piece or string
        if (piece == null) {
            out.print("   ");
        } else {
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) { //FIX COLORS LATER
                out.print(SET_TEXT_COLOR_GREEN);
            } else {
                out.print(SET_TEXT_COLOR_RED);
            }
            out.print(" " + getPieceSymbol(piece) + " ");
        }
    }

    private char getPieceSymbol(ChessPiece piece) {
        //maybe do the actual chess pieces later
        switch (piece.getPieceType()) {
            case KING:
                return 'K';
            case QUEEN:
                return 'Q';
            case ROOK:
                return 'R';
            case BISHOP:
                return 'B';
            case KNIGHT:
                return 'N';
            case PAWN:
                return 'P';
            default:
                return ' ';
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }

    private static void setGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }
}