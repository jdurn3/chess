package service;

import dataAccess.AuthMemoryAccess;
import dataAccess.UserMemoryAccess;
import model.AuthData;
import model.UserData;

public class UserService {
    public AuthData register(UserData user) {
        new UserMemoryAccess().createUser(user);
        String username = user.username();
        return new AuthMemoryAccess().createAuth(username);
    }
    public AuthData login(UserData user) {}
    public void logout(UserData user) {}

}
