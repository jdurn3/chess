package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.Collection;


public class GameService {
    public Collection<GameData> listGames(String authToken, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        authDAO.getAuth(authToken);
        return gameDAO.listGames();
    }

    public int createGame(String authToken, String gameName, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        authDAO.getAuth(authToken);
        return gameDAO.createGame(gameName);
    }

    public GameData joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        AuthData user = authDAO.getAuth(authToken);
        String username = user.username();
        GameData game = gameDAO.getGame(gameID);
        gameDAO.checkPlayer(game, playerColor);
        return gameDAO.joinGame(game, gameID, playerColor, username);
    }
    public void clear(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) throws DataAccessException {
       gameDAO.clear();
       authDAO.clear();
       userDAO.clear();
    }
}
