package dataAccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public void deleteAuth(String authToken) {

    }
}
