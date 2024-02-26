package dataAccess;
import model.UserData;
import server.Constants;

import java.util.HashMap;

public class UserMemoryAccess {
    final private HashMap<String, UserData> users = new HashMap<>();

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

}
