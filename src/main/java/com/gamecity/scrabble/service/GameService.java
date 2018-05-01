package com.gamecity.scrabble.service;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.PlayHelper;
import com.gamecity.scrabble.model.Rack;

public interface GameService
{
    void start(Board board);

    void play(Rack rack);

    void validateRack(Rack rack);

    void validateCurrentPlayer(User user, Long userId);

    void calculateScore(Board board, Rack rack, Long playTime);

    void assignNextUser(Board board);

    void locateRackTiles(PlayHelper helper, Rack rack);

    void findWords(PlayHelper helper);
}
