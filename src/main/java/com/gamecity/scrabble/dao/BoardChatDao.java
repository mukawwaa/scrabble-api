package com.gamecity.scrabble.dao;

import java.util.List;

import com.gamecity.scrabble.entity.BoardChat;

public interface BoardChatDao extends BaseDao<BoardChat>
{
    Integer getMessageCount(Long boardId);

    List<BoardChat> getMessages(Long boardId);
}
