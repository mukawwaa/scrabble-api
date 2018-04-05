package com.gamecity.scrabble.dao;

import com.gamecity.scrabble.entity.cassandra.BoardUserCounter;

public interface BoardUserCounterRepository
{
    BoardUserCounter addPlayer(BoardUserCounter counter);

    BoardUserCounter removePlayer(BoardUserCounter counter);

    BoardUserCounter getBoardUserCounter(Long boardId);
}
