package serviceTests;


import chess.ChessGame;
import dataAccess.AuthMemoryAccess;
import dataAccess.DataAccessException;
import dataAccess.GameMemoryAccess;
import dataAccess.UserMemoryAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

public class ServiceTests {
    @Test
    public void testClearMethod() throws DataAccessException {
        GameMemoryAccess gameDAO = new GameMemoryAccess();
        UserMemoryAccess userDAO = new UserMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();

        gameDAO.createGame("ChessMaster");
        UserData user = new UserData("BOB", "password", "Bob@hotmail.com");
        userDAO.createUser(user);
        authDAO.createAuth("BOB");

        new GameService().clear(userDAO, gameDAO, authDAO);
        assertThrows(DataAccessException.class, () -> userDAO.getUser(user.username()));
    }

    @Test
    public void positiveRegister() throws DataAccessException {
        UserMemoryAccess userDAO = new UserMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        UserData user = new UserData("BOB", "password", "Bob@hotmail.com");

        new UserService().register(user, userDAO, authDAO);

        assertEquals(user, userDAO.getUser(user.username()));
    }

    @Test
    public void negativeRegister() throws DataAccessException {
        UserMemoryAccess userDAO = new UserMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        UserService serverFacade = new UserService();

        serverFacade.register(new UserData("BOB", "password", "Bob@hotmail.com"), userDAO, authDAO);

        // Attempting to register a user with the same username should throw an exception
        assertThrows(DataAccessException.class, () ->
                serverFacade.register(new UserData("BOB", "password2", "Bob2@hotmail.com"), userDAO, authDAO));

    }


    @Test
    public void positiveLogin() throws DataAccessException {
        UserMemoryAccess userDAO = new UserMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        UserService serverFacade = new UserService();
        UserData user = new UserData("BOB", "password", "Bob@hotmail.com");
        AuthData auth = serverFacade.register(user, userDAO, authDAO);

        serverFacade.logout(auth.authToken(), authDAO);
        serverFacade.login(user, userDAO, authDAO);

        // Retrieve the currently logged-in user
        UserData loggedInUser = userDAO.getUser(user.username());

        // Ensure that the relevant fields match
        assertEquals(user.username(), loggedInUser.username());
        assertEquals(user.email(), loggedInUser.email());
    }

    @Test
    public void negativeLogin() throws DataAccessException {
        UserMemoryAccess userDAO = new UserMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        UserService serverFacade = new UserService();
        UserData user = new UserData("BOB", "password", "Bob@hotmail.com");
        UserData incorrect_user = new UserData("BOBBIE", "password", "Bob@hotmail.com");
        AuthData auth = serverFacade.register(user, userDAO, authDAO);


        serverFacade.logout(auth.authToken(), authDAO);

        assertThrows(DataAccessException.class, () ->
                serverFacade.login(incorrect_user, userDAO, authDAO));
    }

    @Test
    public void positiveLogout() throws DataAccessException {
        UserMemoryAccess userDAO = new UserMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        UserService serverFacade = new UserService();
        UserData user = new UserData("BOB", "password", "Bob@hotmail.com");
        AuthData auth = serverFacade.register(user, userDAO, authDAO);

        serverFacade.logout(auth.authToken(), authDAO);

        assertThrows(DataAccessException.class, () ->
                authDAO.getAuth(auth.authToken()));
    }

    @Test
    public void negativeLogout() throws DataAccessException {
        UserMemoryAccess userDAO = new UserMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        UserService serverFacade = new UserService();
        UserData user = new UserData("BOB", "password", "Bob@hotmail.com");
        AuthData auth = serverFacade.register(user, userDAO, authDAO);

        assertThrows(DataAccessException.class, () ->
                serverFacade.logout("auth.authToken()", authDAO));
    }

    @Test
    public void positiveCreateGame() throws DataAccessException {
        GameMemoryAccess gameDAO = new GameMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        GameService serverFacade = new GameService();
        String username = "Bob";
        AuthData auth = authDAO.createAuth(username);
        String gameName = "game 1";

        int gameID = serverFacade.createGame(auth.authToken(), gameName, authDAO, gameDAO);

        GameData loggedGameID = gameDAO.getGame(gameID);

        assertEquals(gameID, loggedGameID.gameID());

    }

    @Test
    public void negativeCreateGame() throws DataAccessException {
        GameMemoryAccess gameDAO = new GameMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        GameService serverFacade = new GameService();
        String auth = "permission";
        String gameName = "game 1";

        assertThrows(DataAccessException.class, () ->
                serverFacade.createGame(auth, gameName, authDAO, gameDAO));
    }
    @Test
    public void positiveListGames() throws DataAccessException {
        GameMemoryAccess gameDAO = new GameMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        GameService serverFacade = new GameService();
        HashMap<Integer, GameData> games = new HashMap<>();

        String username = "Bob";
        AuthData auth = authDAO.createAuth(username);
        String gameName = "game 1";
        String username2 = "Dale";
        AuthData auth2 = authDAO.createAuth(username2);
        String gameName2 = "game 2";

        int game1 = serverFacade.createGame(auth.authToken(), gameName, authDAO, gameDAO);
        int game2 = serverFacade.createGame(auth2.authToken(), gameName2, authDAO, gameDAO);

        games.put(game1, gameDAO.getGame(game1));
        games.put(game2, gameDAO.getGame(game2));

        assertIterableEquals(games.values(), serverFacade.listGames(auth.authToken(), authDAO, gameDAO));
    }
    @Test
    public void negativeListGames() throws DataAccessException {
        GameMemoryAccess gameDAO = new GameMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        GameService serverFacade = new GameService();
        HashMap<Integer, GameData> games = new HashMap<>();

        String username = "Bob";
        AuthData auth = authDAO.createAuth(username);
        String gameName = "game 1";
        String username2 = "Dale";
        AuthData auth2 = authDAO.createAuth(username2);
        String gameName2 = "game 2";

       int game1 = serverFacade.createGame(auth.authToken(), gameName, authDAO, gameDAO);
       int game2 = serverFacade.createGame(auth2.authToken(), gameName2, authDAO, gameDAO);

       games.put(game1, gameDAO.getGame(game1));
       games.put(game2, gameDAO.getGame(game2));

        assertThrows(DataAccessException.class, () ->
                serverFacade.listGames("invalid", authDAO, gameDAO));
    }

    @Test
    public void positiveJoinGame() throws DataAccessException {
        GameMemoryAccess gameDAO = new GameMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        GameService serverFacade = new GameService();
        String username = "Bob";
        AuthData auth = authDAO.createAuth(username);
        String gameName = "game 1";

        int gameID = serverFacade.createGame(auth.authToken(), gameName, authDAO, gameDAO);

        GameData newGame = serverFacade.joinGame(auth.authToken(), ChessGame.TeamColor.BLACK, gameID, authDAO, gameDAO);

        assertEquals(gameID, newGame.gameID());
    }
    @Test
    public void negativeJoinGame() throws DataAccessException {
        GameMemoryAccess gameDAO = new GameMemoryAccess();
        AuthMemoryAccess authDAO = new AuthMemoryAccess();
        GameService serverFacade = new GameService();

        String username = "Bob";
        AuthData auth = authDAO.createAuth(username);
        String gameName = "game 1";
        String username2 = "Dale";
        AuthData auth2 = authDAO.createAuth(username2);

        int gameID = serverFacade.createGame(auth.authToken(), gameName, authDAO, gameDAO);

        serverFacade.joinGame(auth.authToken(), ChessGame.TeamColor.BLACK, gameID, authDAO, gameDAO);

        assertThrows(DataAccessException.class, () ->
                serverFacade.joinGame(auth2.authToken(), ChessGame.TeamColor.BLACK, gameID, authDAO, gameDAO));
    }
}
