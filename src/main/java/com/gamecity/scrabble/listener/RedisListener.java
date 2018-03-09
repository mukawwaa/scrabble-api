package com.gamecity.scrabble.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gamecity.scrabble.entity.BoardUserHistory;
import com.gamecity.scrabble.service.BoardService;

@Component(value = "redisListener")
public class RedisListener
{
    private static Logger logger = LoggerFactory.getLogger(RedisListener.class);

    @Autowired
    private BoardService boardService;

    @Transactional
    public void receiveUserUpdate(Object boardUserHistory)
    {
        try
        {
            boardService.updateBoardUser((BoardUserHistory) boardUserHistory);
        }
        catch (Exception e)
        {
            logger.error("Exception : {} {}", e.getMessage(), e);
        }
    }
}
