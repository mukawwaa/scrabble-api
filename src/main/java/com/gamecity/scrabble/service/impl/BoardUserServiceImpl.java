package com.gamecity.scrabble.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gamecity.scrabble.dao.BoardUserDao;
import com.gamecity.scrabble.entity.BoardUser;
import com.gamecity.scrabble.service.BoardUserService;

@Service(value = "boardUserService")
public class BoardUserServiceImpl extends AbstractServiceImpl<BoardUser, BoardUserDao> implements BoardUserService
{
    @Override
    public BoardUser loadByUserId(Long boardId, Long userId)
    {
        return baseDao.loadByUserId(boardId, userId);
    }

    @Override
    public BoardUser getNextUser(Long boardId, Integer currentOrder)
    {
        return baseDao.getNextUser(boardId, currentOrder);
    }

    @Override
    public List<BoardUser> loadAllActiveUsers(Long boardId)
    {
        return baseDao.loadAllActiveUsers(boardId);
    }
}
