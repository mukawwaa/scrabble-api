package com.gamecity.scrabble.dao.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.gamecity.scrabble.Constants;
import com.gamecity.scrabble.dao.RedisRepository;
import com.gamecity.scrabble.entity.BoardChat;
import com.gamecity.scrabble.model.BoardContent;
import com.gamecity.scrabble.model.ChatMessage;

@Repository(value = "redisRepository")
public class RedisRepositoryImpl implements RedisRepository
{
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void sendChatMessage(BoardChat chat)
    {
        redisTemplate.boundListOps(Constants.RedisListener.BOARD_CHAT + ":" + chat.getBoard().getId()).rightPush(new ChatMessage(chat));
        redisTemplate.convertAndSend(Constants.RedisListener.BOARD_CHAT, chat);
    }

    @Override
    public void sendBoardContent(BoardContent content)
    {
        redisTemplate.boundListOps(Constants.RedisListener.BOARD_CONTENT + ":" + content.getBoardId()).rightPush(content);
        redisTemplate.convertAndSend(Constants.RedisListener.BOARD_CONTENT, content);
    }

    @Override
    public List<ChatMessage> getChatMessages(Long boardId, Integer index)
    {
        BoundListOperations<String, ChatMessage> chats = redisTemplate.boundListOps(Constants.RedisListener.BOARD_CHAT + ":" + boardId);
        return chats.range(index - 1, -1).stream().collect(Collectors.toList());
    }

    @Override
    public BoardContent getBoardContent(Long boardId, Integer orderNo)
    {
        BoundListOperations<String, BoardContent> contents = redisTemplate.boundListOps(Constants.RedisListener.BOARD_CONTENT + ":" + boardId);
        return contents.range(orderNo, -1).stream().findFirst().get();
    }
}
