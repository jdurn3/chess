package dataAccess;
import chess.ChessGame;
import model.GameData;
import java.util.HashSet;
import java.util.Set;

import java.util.HashMap;

public class GameMemoryAccess {

    final private HashMap<Integer, GameData> games = new HashMap<>();

    public void clear() {
        games.clear();
    }

}