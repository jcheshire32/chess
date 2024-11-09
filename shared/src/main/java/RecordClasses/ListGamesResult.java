package RecordClasses;

import model.GameData;

import java.util.List;

//delete the ones I don't use, make the other top level
//    public record ListGamesRequest(AuthData authToken){}
public record ListGamesResult(List<GameData> games) {
}
