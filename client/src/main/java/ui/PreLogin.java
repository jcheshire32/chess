package ui;

import RecordClasses.LoginRequest;
import RecordClasses.LoginResult;
import RecordClasses.RegisterRequest;
import RecordClasses.RegisterResult;
import model.AuthData;
import model.UserData;
import web.ServerFacade;

import java.util.Scanner;

public class PreLogin {

    public String Run(ServerFacade facade) { //main class handles url stuff
        while(true) { //break when they say quit
            System.out.print("[Logged Out]>>> ");
            String input_line = new Scanner(System.in).nextLine();
            String[] inputs = input_line.split(" ");
            String command = inputs[0];
            switch(command){
                case "register":
                    if (inputs.length != 4){ //add a check for if the username is taken
                        System.out.println("[ERROR] You must use this format: register <USERNAME> <PASSWORD> <EMAIL>");
                    } else {
                        try {
                            RegisterRequest user = new RegisterRequest(inputs[1], inputs[2], inputs[3]);
                            RegisterResult registered_user = facade.register(user);
                            if (registered_user.message() != null){
                                System.out.println(registered_user.message());
                                continue;
                            }
                            return registered_user.authToken(); //returning authtoken, shortened by IJ
                        } catch (Exception e) {
                            System.out.println(e); //specify later, Dont show user the stacktrace
                        }
                    }
                    break;
                case "login":
                    if (inputs.length != 3){ //or a user not found
                        System.out.println("[ERROR] You must use this format: login <username> <password>");
                    } else {
                        LoginRequest request = new LoginRequest(inputs[1],inputs[2]);
                        LoginResult logResult = facade.login(request);
                        if (logResult.message() != null){
                            System.out.println(logResult.message());
                            continue;
                        }
                        String authToken = logResult.authToken();
                        return authToken; //handle failure?
                    }
                    break;
                case "quit":
                    System.exit(0);
                    break;
                case "help":
                    System.out.println("register <USERNAME> <PASSWORD> <EMAIL>");
                    System.out.println("login <USERNAME> <PASSWORD> ");
                    System.out.println("quit - exit ");
                    System.out.println("help - list commands ");
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }
}
