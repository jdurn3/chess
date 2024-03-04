package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear();
    int createGame(String gameName);
    Collection<GameData> listGames();
    GameData getGame(int gameID) throws DataAccessException;
    GameData checkPlayer(GameData game, ChessGame.TeamColor playerColor) throws DataAccessException;
    GameData joinGame(GameData game, int gameID, ChessGame.TeamColor playerColor, String username);


}
