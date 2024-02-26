package service;

import chess.ChessGame;
import dataAccess.AuthMemoryAccess;
import dataAccess.DataAccessException;
import dataAccess.GameMemoryAccess;
import dataAccess.UserMemoryAccess;
import model.AuthData;
import model.GameData;

import java.util.Collection;


public class GameService {
    public Collection<GameData> listGames(String authToken, AuthMemoryAccess authDAO, GameMemoryAccess gameDAO) throws DataAccessException {
        authDAO.getAuth(authToken);
        return gameDAO.listGames();
    }

    public int createGame(String authToken, String gameName, AuthMemoryAccess authDAO, GameMemoryAccess gameDAO) throws DataAccessException {
        authDAO.getAuth(authToken);
        return gameDAO.createGame(gameName);
    }

    public GameData joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID, AuthMemoryAccess authDAO, GameMemoryAccess gameDAO) throws DataAccessException {
        AuthData user = authDAO.getAuth(authToken);
        String username = user.username();
        GameData game = gameDAO.getGame(gameID);
        gameDAO.checkPlayer(game, playerColor);
        return gameDAO.joinGame(game, gameID, playerColor, username);
    }
    public void clear(UserMemoryAccess userDAO, GameMemoryAccess gameDAO, AuthMemoryAccess authDAO) {
       gameDAO.clear();
       authDAO.clear();
       userDAO.clear();
    }
}
