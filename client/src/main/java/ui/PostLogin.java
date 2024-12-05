package ui;

import chess.ChessGame;
import model.GameData;
import web.ServerFacade;

import java.util.*;

public class PostLogin {
    List<GameData> games;
    HashMap<Integer, GameData> map = new HashMap<>();
//MAKE THESE ALL RETURN THE GAME

    public ChessGame run(ServerFacade facade, String authToken) {
        while (true) { // break on quit again
            System.out.print("[Logged in]>>> ");
            String inputLine = new Scanner(System.in).nextLine();
            String[] inputs = inputLine.split(" ");
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
                    helpMenu();
                    break;
                case "logout":
                    facade.logout(authToken);
                    return null; //changed from return true when function was a boolean not a chessgame
                case "create":
                    gameCreator(facade, inputs);
                    break;
                case "list":
                    gameLister(facade);
                    break;
                    //join based on numbered list id, not actual game id
                case "join":
                    gameJoiner(facade, inputs);
                    //they are now in gameplay mode
                    break;
                case "observe":
                    gameObserver(inputs);
                    //they are now in gameplay mode
                    break;
            }
        }
    }

    private void gameObserver(String[] inputs) {
        if (inputs.length != 2){
            System.out.println("[ERROR] You must use this format: observe <ID>");
        } else {
            int gameNum;
            try {
                gameNum = Integer.parseInt(inputs[1]);
            } catch (NumberFormatException e){
                System.out.println("[ERROR] Must be a number");
                return;
            }
            if (gameNum < 1 || gameNum > games.size()) {
                System.out.println("[ERROR] Invalid game number");
                return;
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
    }

    private void gameJoiner(ServerFacade facade, String[] inputs) {
        if (inputs.length != 3) { //add more stuff
            System.out.println("[ERROR] You must use this format: join <ID> [WHITE|BLACK]");
        }
        else {
            int id;
            try {
                id = Integer.parseInt(inputs[1]); //throws exception if not a number
            } catch (NumberFormatException e){
                System.out.println("[ERROR] Must be a number");
                return;
            }
            if (id < 1 || id > map.size()){
                System.out.println("[ERROR] invalid game number");
                return;
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
    }

    private void gameLister(ServerFacade facade) {
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
    }

    private static void gameCreator(ServerFacade facade, String[] inputs) {
        if (inputs.length != 2){
            System.out.println("[ERROR] You must use this format: create <NAME>");
        }
        else {
            String name = inputs[1];
            facade.createGame(name);
            System.out.println("Game created successfully");
        }
    }

    private static void helpMenu() {
        System.out.println("create <NAME> - a game ");
        System.out.println("list - games ");
        System.out.println("join <ID> [WHITE|BLACK] - a game ");
        System.out.println("observe <ID> - a game ");
        System.out.println("logout - when you are done");
        System.out.println("help - list commands ");
    }
}
