import chess.*;
import ui.Board;
import ui.GamePlay;
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
        GamePlay gamePlay = new GamePlay();
        ChessGame game;

        //this works for now but idk if it will update with websocket and stuff
        while (true) {
            String authToken = prelogin.run(facade);
            game = postLogin.run(facade, authToken);
            while (true) {
                if (game == null) {
                    prelogin.run(facade);
                }
                if (game != null){
                    if (!gamePlay.run(facade, authToken, game)){
                        break;
                    }
                }
            }
            if (authToken == null) {
                break;
            }
        }
    }
}