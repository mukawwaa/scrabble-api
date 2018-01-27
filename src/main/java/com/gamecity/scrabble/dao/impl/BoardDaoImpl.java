package com.gamecity.scrabble.dao.impl;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.dao.BoardDao;
import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;

@Repository(value = "boardDao")
public class BoardDaoImpl extends AbstractDaoImpl<Board> implements BoardDao
{
    @Override
    public List<Board> getAllByStatus(EnumSet<BoardStatus> statusList)
    {
        return listByNamedQuery("loadAllByStatus", Arrays.asList("statusList"), statusList);
    }

    @Override
    public List<Board> getUserBoardsByStatus(Long userId, EnumSet<BoardStatus> statusList)
    {
        return listByNamedQuery("loadByUserAndStatus", Arrays.asList("userId", "statusList"), userId, statusList);
    }

    @Override
    public void stopExpired()
    {
        executeByNamedQuery("stopExpired", Arrays.asList("status"), BoardStatus.FINISHED);
    }
}
