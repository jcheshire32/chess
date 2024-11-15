package ui;

import RecordClasses.CreateGameRequest;
import RecordClasses.CreateGameResult;
import RecordClasses.ListGamesResult;
import RecordClasses.LogoutResult;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import web.ServerFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostLogin {

    public Boolean Run(ServerFacade facade, String authToken) {
        while (true) { // break on quit again
            System.out.println("[Logged in]>>> ");
            String input_line = new Scanner(System.in).nextLine();
            String[] inputs = input_line.split(" ");
            String command = inputs[0];
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
                        System.out.println("[Logged in]>>> "); //might delete
                        //return?
                    }
                    break;
                case "list":
                    List<GameData> games;
                    games = facade.listGames();
                    for (GameData game: games) {
                        System.out.println(game);
                    }
                    break;
                case "join":
                    if (inputs.length != 3){
                        System.out.println("[ERROR] You must use this format: join <ID> [WHITE|BLACK]");
                    }
                    else {
                        int id = Integer.parseInt(inputs[1]); //throws exception if not a number
                        String color = inputs[2].toUpperCase();
                        if (color.equals("BLACK")){
                            facade.joinGame(id, ChessGame.TeamColor.BLACK);
                        } else if (color.equals("WHITE")) {
                            facade.joinGame(id, ChessGame.TeamColor.WHITE);
                        }
                        else {
                            System.out.println("[ERROR] Invalid color");
                        }
                    }
                    break;
                case "observe":
                    break;
            }
        }
    }
}
