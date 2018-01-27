package com.gamecity.scrabble.service;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.model.Rack;

public interface GameService
{
    void start(Board board);

    Integer play(Rack rack);
}
