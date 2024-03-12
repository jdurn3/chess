package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import server.Constants;

import java.util.Collection;


public class GameService {
    public Collection<GameData> listGames(String authToken, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException(Constants.UNAUTHORIZED);
        }
        return gameDAO.listGames();
    }

    public int createGame(String authToken, String gameName, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException(Constants.UNAUTHORIZED);
        }
        return gameDAO.createGame(gameName);
    }

    public GameData joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        AuthData user = authDAO.getAuth(authToken);
        if (user == null) {
            throw new DataAccessException(Constants.UNAUTHORIZED);
        }
        String username = user.username();
        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new DataAccessException(Constants.BAD_REQUEST);

        }
        if (!gameDAO.checkPlayer(game, playerColor)) {
            throw new DataAccessException(Constants.ALREADY_TAKEN);
        }
        return gameDAO.joinGame(game, gameID, playerColor, username);
    }
    public void clear(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) throws DataAccessException {
       gameDAO.clear();
       authDAO.clear();
       userDAO.clear();
    }
}
