package com.gamecity.scrabble.service;

import java.util.List;

import com.gamecity.scrabble.entity.BoardChat;
import com.gamecity.scrabble.model.ChatMessage;

public interface BoardChatService extends BaseService<BoardChat>
{
    void send(Long boardId, Long userId, String message);

    List<ChatMessage> getMessages(Long boardId, Integer index);
}
