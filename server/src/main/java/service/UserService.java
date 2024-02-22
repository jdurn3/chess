package service;

import dataAccess.AuthMemoryAccess;
import dataAccess.DataAccessException;
import dataAccess.UserMemoryAccess;
import model.AuthData;
import model.UserData;

public class UserService {
    public AuthData register(UserData user) throws DataAccessException {
        new UserMemoryAccess().createUser(user);
        String username = user.username();
        new UserMemoryAccess().getUser(username);
        new UserMemoryAccess().createUser(user);
        return new AuthMemoryAccess().createAuth(username);
    }
    public AuthData login(UserData user) throws DataAccessException {
        String username = user.username();
        new UserMemoryAccess().getUser(username);
        return new AuthMemoryAccess().createAuth(username);
    }
    public void logout(String authToken) throws DataAccessException{
        new AuthMemoryAccess().deleteAuth(authToken);

    }

}
