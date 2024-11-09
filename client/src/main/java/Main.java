import chess.*;
import ui.PostLogin;
import ui.PreLogin;
import web.ServerFacade;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ServerFacade facade = new ServerFacade("localhost:8080");//temp url //url? from command line?
        PreLogin prelogin = new PreLogin();
        String authToken = prelogin.Run(facade);//url
//        PostLogin postLogin = new PostLogin(authToken);
    }
}