package web;

import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.*;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    private Session session;

    public WebSocketFacade(String url) {
        try {
            url = url.replace("http", "ws");
            URI uri = new URI(url + "/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();

            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {

                @Override
                public void onMessage(String s) { //make a separate handler or just do it here?
                    ServerMessage message = new Gson().fromJson(s, ServerMessage.class);
                    if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)){

                    } else if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {

                    } else if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {

                    } else {
                        //error
                    }
                }
            });

        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new RuntimeException(e); // find good exception
        }
    }

    public void connect(String authToken, Integer gameID){
        try {
            var command = new Connect(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) {//IOexception from sendText
            //handle
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move){
        try {
            var command = new MakeMove(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e){//IOexception from sendText
            //handle
        }
    }

    public void leave(String authToken, int gameID){
        try {
            var command = new Leave(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) {//IOexception from sendText
            //handle
        }
    }

    public void resign(String authToken, int gameID){
        try {
            var command = new Resign(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) {//IOexception from sendText
            //handle
        }
    }

    //don't actually need this but gets mad if it's deleted
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
