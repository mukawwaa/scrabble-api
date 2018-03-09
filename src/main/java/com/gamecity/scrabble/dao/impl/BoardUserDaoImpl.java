package com.gamecity.scrabble.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.dao.BoardUserDao;
import com.gamecity.scrabble.entity.BoardUser;

@Repository(value = "boardUserDao")
public class BoardUserDaoImpl extends AbstractDaoImpl<BoardUser> implements BoardUserDao
{
    @Override
    public BoardUser loadByUserId(Long boardId, Long userId)
    {
        return findByNamedQuery("loadUserByUserId", Arrays.asList("boardId", "userId"), boardId, userId);
    }

    @Override
    public BoardUser getNextUser(Long boardId, Integer orderNo)
    {
        return findByNamedQuery("loadNextUser", Arrays.asList("boardId", "orderNo"), boardId, orderNo);
    }

    @Override
    public List<BoardUser> loadAllActiveUsers(Long boardId)
    {
        return listByNamedQuery("loadActiveUsersByBoardId", Arrays.asList("boardId"), boardId);
    }
}
