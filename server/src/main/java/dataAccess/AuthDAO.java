package dataAccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
}
