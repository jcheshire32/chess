package ui;

import RecordClasses.CreateGameRequest;
import RecordClasses.CreateGameResult;
import RecordClasses.ListGamesResult;
import RecordClasses.LogoutResult;
import chess.ChessBoard;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import web.ServerFacade;

import java.util.*;

public class PostLogin {
    List<GameData> games;
    HashMap<Integer, GameData> map = new HashMap<>();


    public Boolean Run(ServerFacade facade, String authToken) {
        while (true) { // break on quit again
            System.out.print("[Logged in]>>> ");
            String input_line = new Scanner(System.in).nextLine();
            String[] inputs = input_line.split(" ");
            String command = inputs[0];
            //initialize games under the hood shoutout mckenna
            games = facade.listGames();
            if (!games.isEmpty()) {
                games = facade.listGames();
                map.clear();
                for (int i = 0; i < games.size(); i++) {
                    map.put(i, games.get(i));
                }
            }
            switch(command){
                case "help":
                    System.out.println("create <NAME> - a game ");
                    System.out.println("list - games ");
                    System.out.println("join <ID> [WHITE|BLACK] - a game ");
                    System.out.println("observe <ID> - a game ");
                    System.out.println("logout - when you are done");
                    System.out.println("help - list commands ");
                    break;
                case "logout":
                    facade.logout(authToken);
                    return true;
                case "create":
                    if (inputs.length != 2){
                        System.out.println("[ERROR] You must use this format: create <NAME>");
                    }
                    else {
                        String name = inputs[1];
                        facade.createGame(name);
                        System.out.println("Game created successfully");
                    }
                    break;
                case "list":
                    games = facade.listGames();
                    map.clear();
                    for (int i=0; i< games.size(); i++){
                        map.put(i, games.get(i));
                    }
                    if (map.isEmpty()){
                        System.out.println("[ERROR] No games found");
                    }
                    for (int i=0; i < map.size(); i++) {
                        GameData game = map.get(i); //go off this number in join, printed number is incorrectish
                        System.out.println((i+1) + ". " + game.gameName() + " White: " +
                                game.whiteUsername() + ", Black: " + game.blackUsername());
                    }
                    break;
                    //join based on numbered list id, not actual game id
                case "join":
                    if (inputs.length != 3) { //add more stuff
                        System.out.println("[ERROR] You must use this format: join <ID> [WHITE|BLACK]");
                    }
                    else {
                        int id = Integer.parseInt(inputs[1]); //throws exception if not a number
                        if (id < 1 || id > map.size()){
                            System.out.println("[ERROR} invalid game number");
                        }
                        String color = inputs[2].toUpperCase();
                        GameData selectedGame = map.get(id-1);
                        ChessGame game = selectedGame.game();
                        Board board = new Board();
                        board.updateBoard(game.getBoard().getBoard());
                        // if selected color is taken
                        if (color.equals("BLACK")){
                            if (selectedGame.blackUsername() == null) {
                                facade.joinGame(selectedGame.gameID(), ChessGame.TeamColor.BLACK);
                                System.out.println("Joined game as Black");
                                board.displayBlackBoard();
                            } else {
                                System.out.println("Black player is taken");
                            }
                        } else if (color.equals("WHITE")) {
                            if (selectedGame.whiteUsername() == null) {
                                facade.joinGame(selectedGame.gameID(), ChessGame.TeamColor.WHITE);
                                System.out.println("Joined game as White");
                                board.displayWhiteBoard();
                            } else {
                                System.out.println("White player is taken");
                            }
                        } else {
                            System.out.println("[ERROR] Invalid color");
                        }
                    }
                    break;
                case "observe":
                    if (inputs.length != 2){
                        System.out.println("[ERROR] You must use this format: observe <ID>");
                    } else {
                        int gameNum = Integer.parseInt(inputs[1]);
                        if (gameNum < 1 || gameNum > games.size()) {
                            System.out.println("[ERROR] Invalid game number");
                            break;
                        }
                        GameData selectedGame = games.get(gameNum-1);
                        ChessGame game = selectedGame.game();
                        Board board = new Board();
                        board.updateBoard(game.getBoard().getBoard());
                        System.out.println("Which color's perspective do you want to observe?");
                        String inputGame = new Scanner(System.in).nextLine();
                        String[] inGame = inputGame.split(" ");
                        String color = inGame[0].toUpperCase();
                        if (inGame.length != 1 ){
                            System.out.println("[ERROR] Just type the color, white or black");
                        }
                        if (color.equals("WHITE")) {
                            board.displayWhiteBoard();
                        }
                        else if (color.equals("BLACK")) {
                            board.displayBlackBoard();
                        } else {
                            System.out.println("[ERROR] Invalid color");
                        }
                    }
                    //more code here in phase 6
            }
        }
    }
}
