package dataAccess;
import chess.ChessGame;
import model.GameData;
import server.Constants;

import javax.xml.crypto.Data;
import java.util.*;

public class GameMemoryAccess implements GameDAO{
    private  int nextID = 0;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    public void clear() {
        games.clear();
    }

    public int createGame(String gameName) {
        int gameID = nextID++;
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, game);
        return gameID;
    }

    public Collection<GameData> listGames() {
        return games.values();
    }

    public GameData getGame(int gameID) throws DataAccessException {
        if (games.containsKey(gameID)) {
            return games.get(gameID);
        }
        else {
            throw new DataAccessException(Constants.BAD_REQUEST);
        }
    }

    public GameData checkPlayer(GameData game, ChessGame.TeamColor playerColor) throws DataAccessException {
        if (Objects.equals(playerColor, ChessGame.TeamColor.WHITE)) {
            String user = game.whiteUsername();
            if (user != null) {
                throw new DataAccessException(Constants.ALREADY_TAKEN);
            }
        }
        if (Objects.equals(playerColor, ChessGame.TeamColor.BLACK)) {
            String user = game.blackUsername();
            if (user != null) {
                throw new DataAccessException(Constants.ALREADY_TAKEN);
            }
        }
        return game;
    }

    public GameData joinGame(GameData game, int gameID, ChessGame.TeamColor playerColor, String username) {
        GameData newGame = game;
        if (Objects.equals(playerColor, ChessGame.TeamColor.WHITE)) {
            newGame = new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
        }
       else if ((Objects.equals(playerColor, ChessGame.TeamColor.BLACK))) {
            newGame = new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
        }
        games.remove(gameID);
        games.put(gameID, newGame);
        return newGame;
    }


}
