package com.gamecity.scrabble.test;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.BoardParams;
import com.gamecity.scrabble.model.Rack;
import com.gamecity.scrabble.model.RackTile;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.BoardUserHistoryService;
import com.gamecity.scrabble.service.BoardUserService;
import com.gamecity.scrabble.service.ContentService;
import com.gamecity.scrabble.service.GameService;

public abstract class AbstractGameTest extends AbstractServiceTest
{
    protected Board board;
    protected User creator;
    protected User player;
    protected User admin;
    protected Rule rule;
    protected Rack rack;

    @Autowired
    protected BoardService boardService;

    @Autowired
    protected BoardUserService boardUserService;

    @Autowired
    protected BoardUserHistoryService boardUserHistoryService;

    @Autowired
    protected ContentService contentService;

    @Autowired
    protected GameService gameService;

    public void setUp()
    {
        creator = getCreator();
        player = getPlayer();
        admin = getAdmin();
        rule = getDefaultRule();
    }

    protected void createBoardAndStartGame(User user)
    {
        createBoard(user, 2);
        boardService.join(board.getId(), player.getId());
        sleep(2);
        board = boardService.get(board.getId());
        assertTrue(BoardStatus.STARTED.equals(board.getStatus()));
    }

    protected void playAMove()
    {
        createCorrectRack(board.getId(), creator.getId());
        gameService.play(rack);
    }

    protected void createBoard(User user, Integer userCount)
    {
        BoardParams params = new BoardParams();
        params.setDuration(1);
        params.setName(RandomStringUtils.randomAlphabetic(10));
        params.setRuleId(rule.getId());
        params.setUserCount(userCount);
        params.setUserId(user.getId());
        board = boardService.create(params);
    }

    protected void createCorrectRack(Long boardId, Long userId)
    {
        rack = contentService.getRack(board.getId(), userId);
        int columnNumber = 5;
        for (RackTile rackTile : rack.getTiles())
        {
            rackTile.setColumnNumber(columnNumber);
            rackTile.setRowNumber(8);
            rackTile.setUsed(true);
            columnNumber = columnNumber + 1;
        }
    }

    protected void createWrongRack(Long boardId, Long userId)
    {
        rack = contentService.getRack(board.getId(), userId);
        int columnNumber = 4;
        for (RackTile rackTile : rack.getTiles())
        {
            if (columnNumber % 2 == 0)
            {
                rackTile.setColumnNumber(columnNumber);
                rackTile.setRowNumber(8);
                rackTile.setUsed(true);
            }
            columnNumber = columnNumber + 1;
        }
    }

    protected void fillRack(Long userId)
    {
        rack = contentService.getRack(board.getId(), userId);
    }

    protected void play()
    {
        gameService.play(rack);
    }
}
