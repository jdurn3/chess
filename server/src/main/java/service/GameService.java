package service;

import dataAccess.AuthMemoryAccess;
import dataAccess.GameMemoryAccess;
import dataAccess.UserMemoryAccess;
import model.AuthData;
import model.GameData;

public class GameService {
    public AuthData listGames() {}

    public AuthData createGame(GameData GameID) {}

    public AuthData joinGame(GameData GameID) {}
    public void clear() {
       new GameMemoryAccess().clear();
       new AuthMemoryAccess().clear();
       new UserMemoryAccess().clear();
    }
}
