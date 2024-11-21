import chess.*;
import ui.Board;
import ui.PostLogin;
import ui.PreLogin;
import web.ServerFacade;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        PreLogin prelogin = new PreLogin();
        PostLogin postLogin = new PostLogin();

        while (true) {
            String authToken = prelogin.run(facade);
            while (true) {
                if (postLogin.run(facade, authToken)){
                    break;
                }
                //game UI
            }
        }
    }
}