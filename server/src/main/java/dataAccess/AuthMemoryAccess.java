package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashSet;

public class AuthMemoryAccess {
    final private HashSet<AuthData> authTokens = new HashSet<>();
    public AuthData createAuth(String username) {
        String authToken = "hello";
        AuthData newAuth = new AuthData(authToken, username);
        authTokens.add(newAuth);
        return newAuth;
    }
}
