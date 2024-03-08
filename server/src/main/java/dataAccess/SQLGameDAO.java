package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public class SQLGameDAO implements GameDAO{
    @Override
    public void clear() {

    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData checkPlayer(GameData game, ChessGame.TeamColor playerColor) throws DataAccessException {
        return null;
    }

    @Override
    public GameData joinGame(GameData game, int gameID, ChessGame.TeamColor playerColor, String username) {
        return null;
    }
}
