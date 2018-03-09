package com.gamecity.scrabble.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gamecity.scrabble.dao.BoardUserHistoryDao;
import com.gamecity.scrabble.entity.BoardUserHistory;
import com.gamecity.scrabble.service.BoardUserHistoryService;

@Service(value = "boardUserHistoryService")
public class BoardUserHistoryServiceImpl extends AbstractServiceImpl<BoardUserHistory, BoardUserHistoryDao> implements BoardUserHistoryService
{
    @Override
    public BoardUserHistory loadByUserId(Long boardId, Long userId)
    {
        return baseDao.loadByUserId(boardId, userId);
    }

    @Override
    public List<BoardUserHistory> loadAllWaitingUsers(Long boardId)
    {
        return baseDao.loadAllWaitingUsers(boardId);
    }

    @Override
    public Integer getWaitingUserCount(Long boardId)
    {
        return baseDao.getWaitingUserCount(boardId);
    }
}
