package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import server.Constants;

public class UserService {
    public AuthData register(UserData user, UserDAO userDAO, AuthDAO authDAO) throws DataAccessException {
        String username = user.username();
        if (userDAO.checkUser(username)) {
            throw new DataAccessException(Constants.ALREADY_TAKEN);
        }
        if (user.password() == null) {
            throw new DataAccessException(Constants.BAD_REQUEST);
        }
        userDAO.createUser(user);
        return authDAO.createAuth(username);
    }
    public AuthData login(UserData user, UserDAO userDAO, AuthDAO authDAO) throws DataAccessException {
        String username = user.username();
        UserData selectedUser = userDAO.getUser(username);
        if (selectedUser == null) {
            throw new DataAccessException(Constants.UNAUTHORIZED);
        }
        if (userDAO.checkPassword(selectedUser.password(), user.password())) {
            return authDAO.createAuth(username);
        }
        throw new DataAccessException(Constants.UNAUTHORIZED);
    }

    public void logout(String authToken, AuthDAO authDAO) throws DataAccessException{
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException(Constants.UNAUTHORIZED);
        }
        authDAO.deleteAuth(authToken);

    }

}
