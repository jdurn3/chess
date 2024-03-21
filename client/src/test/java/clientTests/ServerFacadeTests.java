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
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
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
    public void testLogoutPositive() {
        // Positive logout test
        assertDoesNotThrow(facade::logout);
    }

    @Test
    public void testCreateGamePositive() {
        assertDoesNotThrow(() -> facade.createGame("TestGame"));
    }

    @Test
    public void testListGamesPositive() {
        assertDoesNotThrow(facade::listGames);
    }

    @Test
    public void testJoinGamePositive() {
        assertDoesNotThrow(() -> facade.joinGame(123, ChessGame.TeamColor.WHITE));

    }


}
