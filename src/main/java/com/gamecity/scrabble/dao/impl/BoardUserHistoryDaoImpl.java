package com.gamecity.scrabble.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.dao.BoardUserHistoryDao;
import com.gamecity.scrabble.entity.BoardUserHistory;

@Repository(value = "boardUserHistoryDao")
public class BoardUserHistoryDaoImpl extends AbstractDaoImpl<BoardUserHistory> implements BoardUserHistoryDao
{
    @Override
    public BoardUserHistory loadByUserId(Long boardId, Long userId)
    {
        return findByNamedQuery("loadHistoryByUserId", Arrays.asList("boardId", "userId"), boardId, userId);
    }

    @Override
    public List<BoardUserHistory> loadAllWaitingUsers(Long boardId)
    {
        return listByNamedQuery("loadWaitingUsersByBoardId", Arrays.asList("boardId"), boardId);
    }

    @Override
    public Integer getWaitingUserCount(Long boardId)
    {
        return ((Long) findGenericTypeByNamedQuery("getWaitingUserCount", Arrays.asList("boardId"), boardId)).intValue();
    }
}
