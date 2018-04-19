package com.gamecity.scrabble.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamecity.scrabble.dao.BoardChatDao;
import com.gamecity.scrabble.dao.RedisRepository;
import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardChat;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.ChatMessage;
import com.gamecity.scrabble.service.BoardChatService;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.UserService;

@Service(value = "boardChatService")
public class BoardChatServiceImpl extends AbstractServiceImpl<BoardChat, BoardChatDao> implements BoardChatService
{
    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisRepository redisRepository;

    @Transactional
    @Override
    public void send(Long boardId, Long userId, String message)
    {
        Board board = boardService.validateAndGetAvailableBoard(boardId);
        User user = userService.get(userId);
        BoardChat chat = new BoardChat(board, user, message.replace("\"", ""));
        chat = baseDao.save(chat);
        redisRepository.sendChatMessage(chat);
    }

    @Override
    public List<ChatMessage> getMessages(Long boardId, Integer index)
    {
        return redisRepository.getChatMessages(boardId, index);
    }
}
