package ui;

import recordclasses.LoginRequest;
import recordclasses.LoginResult;
import recordclasses.RegisterRequest;
import recordclasses.RegisterResult;
import web.ServerFacade;

import java.util.Scanner;

public class PreLogin {

    public String run(ServerFacade facade) { //main class handles url stuff
        while(true) { //break when they say quit
            System.out.print("[Logged Out]>>> ");
            String inputLine = new Scanner(System.in).nextLine();
            String[] inputs = inputLine.split(" ");
            String command = inputs[0];
            switch(command){
                case "register":
                    try {
                        String authToken = doRegister(inputs, facade);
                        if(authToken != null) {
                            return authToken;
                        }
                    } catch (Exception e) {
                        System.out.println(e); //specify later, Don't show user the stacktrace
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
    private String doRegister(String[] inputs, ServerFacade facade) {
        if (inputs.length != 4){
            System.out.println("[ERROR] You must use this format: register <USERNAME> <PASSWORD> <EMAIL>");
            return null;
        } else {
            RegisterRequest user = new RegisterRequest(inputs[1], inputs[2], inputs[3]);
            RegisterResult registeredUser = facade.register(user);
            if (registeredUser.message() != null){
                System.out.println(registeredUser.message());
                return null;
            }
            return registeredUser.authToken();
        }
    }
}
