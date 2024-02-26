package model;

import chess.ChessGame;

public record Join(
        ChessGame.TeamColor playerColor,
        int gameID

) {}
