package dataAccess;
import model.GameData;
import java.util.HashSet;
import java.util.Set;

import java.util.HashMap;

public class GameMemoryAccess {
    private int nextId = 1;
    final private HashSet<GameData> games = new HashSet<>();

    public void clear() {
        games.clear();
    }

    public void
