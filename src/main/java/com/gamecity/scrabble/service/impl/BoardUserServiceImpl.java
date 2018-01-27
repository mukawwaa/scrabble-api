package com.gamecity.scrabble.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamecity.scrabble.dao.BoardUserDao;
import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardUser;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.BoardUserService;

@Service(value = "boardUserService")
public class BoardUserServiceImpl extends AbstractServiceImpl<BoardUser, BoardUserDao> implements BoardUserService
{
    @Autowired
    private BoardService boardService;

    @Override
    public BoardUser loadByUserId(Long boardId, Long userId)
    {
        return baseDao.loadByUserId(boardId, userId);
    }

    @Override
    public BoardUser getNextUser(Long boardId)
    {
        Board board = boardService.get(boardId);
        int nextOrderNo = (board.getOrderNo() % board.getUserCount()) + 1;
        return baseDao.getNextUser(boardId, nextOrderNo);
    }

    @Override
    public List<BoardUser> loadAllActiveUsers(Long boardId)
    {
        return baseDao.loadAllActiveUsers(boardId);
    }
}
