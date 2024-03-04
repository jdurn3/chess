package dataAccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData user);
    void clear();
    UserData getUser(String username) throws DataAccessException;
    String checkUser(String username) throws DataAccessException;
    String checkPassword(String password, String givenPassword) throws DataAccessException;
    UserData validPassword(UserData user) throws DataAccessException;
}
