package model;

import chess.ChessGame;

public class GameData {
    record GameRecord(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}

}
