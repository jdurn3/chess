package dataAccess;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import server.Constants;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Objects;

public class UserMemoryAccess {
    private final HashMap<String, UserData> users = new HashMap<>();

    public void createUser(UserData user) {
        String username = user.username();
        users.put(username, user);
    }

    public void clear() {
        users.clear();
    }

    public UserData getUser(String username) throws DataAccessException {
        if (users.containsKey(username)) {
            return users.get(username);
        } else {
            throw new DataAccessException(Constants.UNAUTHORIZED);

        }
    }

    public String checkUser(String username) throws DataAccessException {
            if (users.containsKey(username)) {
                throw new DataAccessException(Constants.ALREADY_TAKEN);
            }
            else {
                return username;
            }
        }

    public String checkPassword(String password, String givenPassword) throws DataAccessException {
        if (Objects.equals(password, givenPassword)) {
            return password;
        }
        else {
            throw new DataAccessException(Constants.UNAUTHORIZED);
        }
    }

    public UserData validPassword(UserData user) throws DataAccessException {
        if (user.password() == null ) {
            throw new DataAccessException(Constants.BAD_REQUEST);
        }
        return user;
    }
}
