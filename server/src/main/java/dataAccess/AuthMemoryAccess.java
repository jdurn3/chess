package dataAccess;
import java.util.HashMap;
import java.util.UUID;
import model.AuthData;
import model.GameData;

import java.util.HashSet;

public class AuthMemoryAccess {
    final private HashMap<String, AuthData> authTokens = new HashMap<>();
    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authTokens.put(username, newAuth);
        return newAuth;
    }

    public void clear() {
        authTokens.clear();
    }

    public void deleteAuth(String authToken) {
        authTokens.values().removeIf(authData -> authData.authToken().equals(authToken));
    }
}
