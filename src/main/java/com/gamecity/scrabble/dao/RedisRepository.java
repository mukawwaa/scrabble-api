package com.gamecity.scrabble.dao;

import java.util.List;

import com.gamecity.scrabble.entity.BoardChat;
import com.gamecity.scrabble.entity.BoardUserHistory;
import com.gamecity.scrabble.model.BoardContent;
import com.gamecity.scrabble.model.BoardPlayer;
import com.gamecity.scrabble.model.ChatMessage;

public interface RedisRepository
{
    void sendChatMessage(BoardChat chat);

    void updateBoardUserHistory(BoardUserHistory history);

    List<ChatMessage> getChatMessages(Long boardId, Integer index);

    void sendBoardContent(BoardContent content);

    BoardContent getBoardContent(Long boardId, Integer orderNo);

    void sendBoardPlayers(BoardPlayer player);

    BoardPlayer getBoardPlayers(Long boardId, Integer orderNo);
}
