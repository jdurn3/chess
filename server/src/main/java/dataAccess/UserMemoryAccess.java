package dataAccess;
import model.UserData;
import java.util.HashSet;

public class UserMemoryAccess {
    final private HashSet<UserData> users = new HashSet<>();

    public void createUser(UserData user) {
        users.add(user);
    }

}
