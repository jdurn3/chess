package dataAccess;

import com.google.gson.Gson;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {
    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        executeUpdate(statement, user.username(), user.password(), user.email());
    }

    public SQLUserDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM user;";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to delete users: %s", e.getMessage()));
        }
    }


    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username,password, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    @Override
    public boolean checkUser(String username) throws DataAccessException {
        boolean exists = false;

        try (var conn = DatabaseManager.getConnection()) {
            var query = "SELECT COUNT(*) FROM user WHERE username = ?";
            try (var ps = conn.prepareStatement(query)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        exists = count > 0;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to check username existence: %s", e.getMessage()));
        }

        return exists;
    }

    @Override
    public boolean checkPassword(String password, String givenPassword) throws DataAccessException {
        if (Objects.equals(password, givenPassword)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean validPassword(UserData user) throws DataAccessException {
        boolean used = false;
        String password = user.password();
        try (var conn = DatabaseManager.getConnection()) {
            var query = "SELECT COUNT(*) FROM user WHERE password = ?";
            try (var ps = conn.prepareStatement(query)) {
                ps.setString(1, password);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        used = count > 0;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to check password usage: %s", e.getMessage()));
        }

        return used;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof UserData p) ps.setString(i + 1, p.toString());
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
            CREATE TABLE IF NOT EXISTS  user (
              `username` NOT NULL,
              `password` NOT NULL,
              `email` NOT NULL,
              PRIMARY KEY (`username`),
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

