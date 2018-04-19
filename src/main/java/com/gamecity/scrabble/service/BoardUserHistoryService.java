package com.gamecity.scrabble.service;

import java.util.List;

import com.gamecity.scrabble.entity.BoardUserHistory;
import com.gamecity.scrabble.entity.PlayerAction;
import com.gamecity.scrabble.entity.cassandra.BoardUserCounter;

public interface BoardUserHistoryService extends BaseService<BoardUserHistory>
{
    BoardUserHistory loadLastActionByUserId(Long boardId, Long userId);

    BoardUserCounter calculatePlayerCount(Long boardId, PlayerAction action);

    BoardUserCounter getBoardUserCounter(Long boardId);

    List<BoardUserHistory> loadAllWaitingUsers(Long boardId);
}
