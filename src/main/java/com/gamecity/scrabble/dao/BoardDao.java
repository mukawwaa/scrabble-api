package com.gamecity.scrabble.dao;

import java.util.EnumSet;
import java.util.List;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;

public interface BoardDao extends BaseDao<Board>
{
    List<Board> getAllByStatus(EnumSet<BoardStatus> statusList);

    List<Board> getUserBoardsByStatus(Long userId, EnumSet<BoardStatus> statusList);

    void stopExpired();
}
