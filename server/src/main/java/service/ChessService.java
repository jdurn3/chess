package service;

public class ChessService {
    public void clear() throws ResponseException {
        dataAccess.clear();
    }
}
