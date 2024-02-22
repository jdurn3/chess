package dataAccess;
import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.HashSet;

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
        }
        else {
            throw new DataAccessException("No User Found");

    }

}
