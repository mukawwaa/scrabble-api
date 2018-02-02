package com.gamecity.scrabble.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamecity.scrabble.model.Player;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.ContentService;

@Service(value = "redisListener")
public class RedisListener
{
    @Autowired
    private BoardService boardService;

    @Autowired
    private ContentService contentService;

    @Transactional
    public void receiveUserUpdate(Object playerUpdate)
    {
        Player player = (Player) playerUpdate;
        if (player.isEnabled())
        {
            boardService.createBoardUser(player.getBoardId(), player.getUserId());
        }
        else
        {
            boardService.removeBoardUser(player.getBoardId(), player.getUserId());
        }
        contentService.updatePlayers(player.getBoardId(), 0);
    }
}
