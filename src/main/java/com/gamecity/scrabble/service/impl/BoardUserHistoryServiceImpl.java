package com.gamecity.scrabble.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamecity.scrabble.dao.BoardUserCounterRepository;
import com.gamecity.scrabble.dao.BoardUserHistoryDao;
import com.gamecity.scrabble.entity.BoardUserHistory;
import com.gamecity.scrabble.entity.PlayerAction;
import com.gamecity.scrabble.entity.cassandra.BoardUserCounter;
import com.gamecity.scrabble.service.BoardUserHistoryService;

@Service(value = "boardUserHistoryService")
public class BoardUserHistoryServiceImpl extends AbstractServiceImpl<BoardUserHistory, BoardUserHistoryDao> implements BoardUserHistoryService
{
    @Autowired
    private BoardUserCounterRepository boardUserCounterRepository;

    @Override
    public BoardUserHistory loadLastActionByUserId(Long boardId, Long userId)
    {
        return baseDao.loadLastActionByUserId(boardId, userId);
    }

    @Override
    public BoardUserCounter calculatePlayerCount(Long boardId, PlayerAction action)
    {
        BoardUserCounter counter = new BoardUserCounter();
        counter.setBoardId(boardId);

        if (PlayerAction.LEFT.equals(action))
        {
            counter = boardUserCounterRepository.removePlayer(counter);
        }
        else
        {
            counter = boardUserCounterRepository.addPlayer(counter);
        }

        return counter;
    }

    @Override
    public BoardUserCounter getBoardUserCounter(Long boardId)
    {
        return boardUserCounterRepository.getBoardUserCounter(boardId);
    }

    @Override
    public List<BoardUserHistory> loadAllWaitingUsers(Long boardId)
    {
        return baseDao.loadAllWaitingUsers(boardId);
    }
}
