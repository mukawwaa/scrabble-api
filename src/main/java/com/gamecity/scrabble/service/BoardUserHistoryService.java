package com.gamecity.scrabble.service;

import java.util.List;

import com.gamecity.scrabble.entity.BoardUserHistory;

public interface BoardUserHistoryService extends BaseService<BoardUserHistory>
{
    BoardUserHistory loadByUserId(Long boardId, Long userId);

    List<BoardUserHistory> loadAllWaitingUsers(Long boardId);

    Integer getWaitingUserCount(Long boardId);
}
