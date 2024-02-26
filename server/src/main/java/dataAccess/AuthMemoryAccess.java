package dataAccess;
import java.util.HashMap;
import java.util.UUID;
import model.AuthData;
import model.GameData;
import server.Constants;

import java.util.HashSet;

public class AuthMemoryAccess {
    final private HashMap<String, AuthData> authTokens = new HashMap<>();
    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authTokens.put(authToken, newAuth);
        return newAuth;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authTokens.containsKey(authToken)) {
            return authTokens.get(authToken);
        }
        else {
            throw new DataAccessException(Constants.UNAUTHORIZED);
        }
    }
    public void clear() {
        authTokens.clear();
    }

    public void deleteAuth(String authToken) {
        authTokens.values().removeIf(authData -> authData.authToken().equals(authToken));
    }
}
