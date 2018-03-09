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
import com.gamecity.scrabble.entity.BoardUserHistory;
import com.gamecity.scrabble.model.BoardContent;
import com.gamecity.scrabble.model.BoardPlayer;
import com.gamecity.scrabble.model.ChatMessage;

@Repository(value = "redisRepository")
public class RedisRepositoryImpl implements RedisRepository
{
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void sendChatMessage(BoardChat chat)
    {
        ChatMessage chatMessage = new ChatMessage(chat);
        redisTemplate.boundListOps(Constants.RedisListener.BOARD_CHAT + ":" + chatMessage.getBoardId()).rightPush(chatMessage);
        redisTemplate.convertAndSend(Constants.RedisListener.BOARD_CHAT, chatMessage);
    }

    @Override
    public void updateBoardUserHistory(BoardUserHistory history)
    {
        redisTemplate.boundListOps(Constants.RedisListener.BOARD_USER_HISTORY + ":" + history.getBoardId()).rightPush(history);
        redisTemplate.convertAndSend(Constants.RedisListener.BOARD_USER_HISTORY, history);
    }

    @Override
    public void sendBoardPlayers(BoardPlayer player)
    {
        redisTemplate.boundListOps(Constants.RedisListener.BOARD_PLAYERS + ":" + player.getBoardId()).rightPush(player);
        redisTemplate.convertAndSend(Constants.RedisListener.BOARD_PLAYERS, player);
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
        BoundListOperations<String, Object> chats = redisTemplate.boundListOps(Constants.RedisListener.BOARD_CHAT + ":" + boardId);
        return chats.range(index - 1, -1).stream().map(item -> (ChatMessage) item).collect(Collectors.toList());
    }

    @Override
    public BoardPlayer getBoardPlayers(Long boardId, Integer orderNo)
    {
        BoundListOperations<String, Object> contents = redisTemplate.boundListOps(Constants.RedisListener.BOARD_PLAYERS + ":" + boardId);
        return (BoardPlayer) contents.range(orderNo, -1).stream().findFirst().get();
    }

    @Override
    public BoardContent getBoardContent(Long boardId, Integer orderNo)
    {
        BoundListOperations<String, Object> contents = redisTemplate.boundListOps(Constants.RedisListener.BOARD_CONTENT + ":" + boardId);
        return (BoardContent) contents.range(orderNo, -1).stream().findFirst().get();
    }
}
