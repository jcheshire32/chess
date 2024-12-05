package ui;

import chess.ChessGame;
import web.ServerFacade;

import java.util.Scanner;

public class GamePlay {
//added game as parameter
    public Boolean run(ServerFacade facade, String authToken, ChessGame game) {
        while (true) { // break on quit again
            System.out.print("[Logged in]>>> ");
            String inputLine = new Scanner(System.in).nextLine();
            String[] inputs = inputLine.split(" ");
            String command = inputs[0];

            switch (command) {
                case "help":
                    helpMenu();
                    break;
                case "draw": //redraw chessboard
                    break;
                case "leave":
                    return true;
                case "move": //make move
                    break;
                case "resign":
                    break;
                case "highlight": //highlight legal moves
                    break;
            }
        }
    }
    private static void helpMenu() {
        System.out.println("draw - redraw the board");
        System.out.println("leave - exit the game, this is NOT resigning");
        System.out.println("move <START POSITION> <END POSITION> - move your piece "); //maybe require piece? shouldn't need to but idk
        System.out.println("resign - You are FORFEITING"); //add a confirmation msg to double check?
        System.out.println("highlight <POSITION> - display possible moves for a specific piece"); //maybe require piece? shouldn't need to but idk
        System.out.println("help - list commands ");
    }

//    private Board redrawBoard(ChessGame board) {
//
//    }
}