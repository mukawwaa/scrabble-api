package com.gamecity.scrabble.service;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.model.Rack;

public interface GameService
{
    void start(Board board);

    void play(Rack rack);

    void validateRack(Rack rack);

    void validateCurrentPlayer(Board board, Rack rack);

    void calculateScore(Board board, Rack rack, Long playTime);

    void assignNextUser(Board board);
}
