package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

public class UserService {
    public AuthData register(UserData user, UserDAO userDAO, AuthDAO authDAO) throws DataAccessException {
        String username = user.username();
        userDAO.checkUser(username);
        userDAO.validPassword(user);
        userDAO.createUser(user);
        return authDAO.createAuth(username);
    }
    public AuthData login(UserData user, UserDAO userDAO, AuthDAO authDAO) throws DataAccessException {
        String username = user.username();
        UserData selectedUser = userDAO.getUser(username);
        userDAO.checkPassword(selectedUser.password(), user.password());
        return authDAO.createAuth(username);
    }

    public void logout(String authToken, AuthDAO authDAO) throws DataAccessException{
        authDAO.getAuth(authToken);
        authDAO.deleteAuth(authToken);

    }

}
