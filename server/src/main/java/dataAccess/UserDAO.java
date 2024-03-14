package dataAccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData user) throws DataAccessException;
    void clear() throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    boolean checkUser(String username) throws DataAccessException;
    boolean checkPassword(String password, String givenPassword) throws DataAccessException;
}
