package com.gamecity.scrabble.dao;

import java.util.List;

import com.gamecity.scrabble.entity.BoardUserHistory;

public interface BoardUserHistoryDao extends BaseDao<BoardUserHistory>
{
    BoardUserHistory loadByUserId(Long boardId, Long userId);

    List<BoardUserHistory> loadAllWaitingUsers(Long boardId);

    Integer getWaitingUserCount(Long boardId);
}
