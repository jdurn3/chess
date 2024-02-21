package service;

import dataAccess.GameMemoryAccess;
import model.AuthData;
import model.GameData;

public class GameService {
    public AuthData listGames() {}

    public AuthData createGame(GameData GameID) {}

    public AuthData joinGame(GameData GameID) {}
    public void clear() {
       return GameMemoryAccess.clear();
    }
}
