package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM games;";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to delete users: %s", e.getMessage()));
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();
        String stored_game = new Gson().toJson(game);
        var statement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
        return executeUpdate(statement, gameName, stored_game);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGames(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }


    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGames(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public boolean checkPlayer(GameData game, ChessGame.TeamColor playerColor) throws DataAccessException {
        if (Objects.equals(playerColor, ChessGame.TeamColor.WHITE)) {
            String user = game.whiteUsername();
            return user == null;
        }
        if (Objects.equals(playerColor, ChessGame.TeamColor.BLACK)) {
            String user = game.blackUsername();
            return user == null;
        }
        return true;
    }

    @Override
    public GameData joinGame(GameData game, int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            if (Objects.equals(ChessGame.TeamColor.BLACK, playerColor)) {
                var query = "UPDATE games SET blackUsername = ? WHERE gameID = ?";
                try (var ps = conn.prepareStatement(query)) {
                    ps.setString(1, username);
                    ps.setInt(2, gameID);
                    ps.executeUpdate();
                }
            }
            else if (Objects.equals(ChessGame.TeamColor.WHITE, playerColor)) {
                var query = "UPDATE games SET whiteUsername = ? WHERE gameID = ?";
                try (var ps = conn.prepareStatement(query)) {
                    ps.setString(1, username);
                    ps.setInt(2, gameID);
                    ps.executeUpdate();
                }
            }
            } catch(Exception e){
                throw new DataAccessException(String.format("Unable to update blackUsername: %s", e.getMessage()));
            }
        return getGame(gameID);
    }

    private GameData readGames(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var json = rs.getString("game");
        ChessGame game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof GameData p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
