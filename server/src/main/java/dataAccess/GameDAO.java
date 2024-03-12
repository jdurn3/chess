package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    boolean checkPlayer(GameData game, ChessGame.TeamColor playerColor) throws DataAccessException;
    GameData joinGame(GameData game, int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException;


}
