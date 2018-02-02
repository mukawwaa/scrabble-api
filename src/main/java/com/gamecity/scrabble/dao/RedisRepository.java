package com.gamecity.scrabble.dao;

import java.util.List;

import com.gamecity.scrabble.entity.BoardChat;
import com.gamecity.scrabble.model.BoardContent;
import com.gamecity.scrabble.model.BoardPlayer;
import com.gamecity.scrabble.model.ChatMessage;
import com.gamecity.scrabble.model.Player;

public interface RedisRepository
{
    void sendChatMessage(BoardChat chat);

    List<ChatMessage> getChatMessages(Long boardId, Integer index);

    void updatePlayer(Player player);

    void sendBoardContent(BoardContent content);

    BoardContent getBoardContent(Long boardId, Integer orderNo);

    void sendBoardPlayers(BoardPlayer player);

    BoardPlayer getBoardPlayers(Long boardId, Integer orderNo);
}
