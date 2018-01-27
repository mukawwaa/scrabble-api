package com.gamecity.scrabble.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.dao.BoardChatDao;
import com.gamecity.scrabble.entity.BoardChat;

@Repository(value = "boardChatDao")
public class BoardChatDaoImpl extends AbstractDaoImpl<BoardChat> implements BoardChatDao
{
    @Override
    public Integer getMessageCount(Long boardId)
    {
        Long count = findGenericTypeByNamedQuery("countByBoardId", Arrays.asList("boardId"), boardId);
        return count.intValue();
    }

    @Override
    public List<BoardChat> getMessages(Long boardId)
    {
        return listByNamedQuery("loadMessagesByBoardId", Arrays.asList("boardId"), boardId);
    }
}
