package service;

import dataAccess.AuthMemoryAccess;
import dataAccess.DataAccessException;
import dataAccess.GameMemoryAccess;
import dataAccess.UserMemoryAccess;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {
    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        new AuthMemoryAccess().getAuth(authToken);
        return new GameMemoryAccess().listGames();
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        new AuthMemoryAccess().getAuth(authToken);
        return new GameMemoryAccess().createGame(gameName);
    }

    public GameData joinGame(String authToken, String playerColor, int gameID) throws DataAccessException {
        AuthData user = new AuthMemoryAccess().getAuth(authToken);
        String username = user.username();
        GameData game = new GameMemoryAccess().getGame(gameID);
        new GameMemoryAccess().checkPlayer(game, playerColor);
        return new GameMemoryAccess().joinGame(game, gameID, playerColor, username);
    }
    public void clear() {
       new GameMemoryAccess().clear();
       new AuthMemoryAccess().clear();
       new UserMemoryAccess().clear();
    }
}
