package clientTests;

import chess.ChessGame;
import exception.DataAccessException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:" + port;
        facade = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearServerState() throws DataAccessException {
        // Method to clear the server state
        facade.clear();

    }


    @Test
    public void testRegisterPositive() {
        UserData user = new UserData("John", "Doe", "john.doe@example.com");
        assertDoesNotThrow(() -> facade.register(user));
    }
    @Test
    public void testRegisterNegative() {
        // Testing with null user object
        assertThrows(DataAccessException.class, () -> facade.register(null));
    }
    @Test
    public void testLoginPositive() throws DataAccessException {
        // Positive login test
        UserData user = new UserData("John", "Doe", "john.doe@example.com");
        facade.register(user);
        assertDoesNotThrow(() -> facade.login("John", "Doe"));
    }

    @Test
    public void testLoginNegative() throws DataAccessException {
        // Negative login test with invalid credentials
        UserData user = new UserData("John", "Doe", "john.doe@example.com");
        facade.register(user);
        assertThrows(DataAccessException.class, () -> facade.login("invalidUsername", "invalidPassword"));
    }

    @Test
    public void testLogoutPositive() throws DataAccessException {
        // Positive logout test
        UserData user = new UserData("John", "Doe", "john.doe@example.com");
        facade.register(user);
        assertDoesNotThrow(facade::logout);
    }

    @Test
    public void testCreateGamePositive() throws DataAccessException {
        UserData user = new UserData("John", "Doe", "john.doe@example.com");
        facade.register(user);
        assertDoesNotThrow(() -> facade.createGame("TestGame"));
    }

    @Test
    public void testListGamesPositive() throws DataAccessException {
        UserData user = new UserData("John", "Doe", "john.doe@example.com");
        facade.register(user);
        assertDoesNotThrow(facade::listGames);
    }

    @Test
    public void testJoinGamePositive() throws DataAccessException {
        UserData user = new UserData("John", "Doe", "john.doe@example.com");
        facade.register(user);
        GameData game = facade.createGame("TestGame");
        assertDoesNotThrow(() -> facade.joinGame(game.gameID(), ChessGame.TeamColor.WHITE));

    }
    @Test
    public void testLogoutNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, facade::logout);
    }

    @Test
    public void testCreateGameNegative() throws DataAccessException {
        UserData user = new UserData("John", "Doe", "john.doe@example.com");
        facade.register(user);
        assertThrows(DataAccessException.class, () -> facade.createGame(null));
    }

    @Test
    public void testListGamesNegativeWithoutLogin() throws DataAccessException {
        assertThrows(DataAccessException.class, facade::listGames);
    }

    @Test
    public void testJoinGameNegative() throws DataAccessException {
        UserData user = new UserData("John", "Doe", "john.doe@example.com");
        facade.register(user);
        GameData game = facade.createGame("TestGame");
        assertThrows(DataAccessException.class, () -> facade.joinGame(123, ChessGame.TeamColor.WHITE));
    }

}
