package com.gamecity.scrabble.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.BoardParams;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.exception.GameError;
import com.gamecity.scrabble.service.exception.GameException;

public class BoardTest extends AbstractServiceTest
{
    private User creator;
    private User player;
    private Rule rule;

    @Autowired
    private BoardService boardService;

    @Before
    public void init()
    {
        creator = getCreator();
        rule = getDefaultRule();
        player = getPlayer();
    }

    @Test
    public void testCreateNewBoard()
    {
        Board board = createBoard(2);
        assertNotNull(board.getId());
    }

    @Test
    public void testJoinBoard()
    {
        Board board = createBoard(3);
        assertNotNull(board.getId());
        boardService.join(board.getId(), player.getId());
    }

    @Test
    public void testJoinBoardWithAlreadyJoinedUserError()
    {
        Board board = createBoard(3);
        assertNotNull(board.getId());
        boardService.join(board.getId(), player.getId());
        try
        {
            boardService.join(board.getId(), player.getId());
            assertEquals(true, false);
        }
        catch (GameException e)
        {
            assertEquals(e.getErrorCode(), GameError.ALREADY_ON_BOARD.getCode());
        }
    }

    @Test
    public void testLeaveBoard()
    {
        Board board = createBoard(3);
        assertNotNull(board.getId());
        boardService.join(board.getId(), player.getId());
        boardService.leave(board.getId(), player.getId());
    }

    @Test
    public void testLeaveBoardWithNotJoinedUserError()
    {
        Board board = createBoard(3);
        assertNotNull(board.getId());
        try
        {
            boardService.leave(board.getId(), player.getId());
            assertEquals(true, false);
        }
        catch (GameException e)
        {
            assertEquals(e.getErrorCode(), GameError.NOT_ON_BOARD.getCode());
        }
    }

    @Test
    public void testBoardStarted()
    {
        Board board = createBoard(2);
        assertNotNull(board.getId());
        boardService.join(board.getId(), player.getId());
        board = boardService.get(board.getId());
        assertTrue(BoardStatus.STARTED.equals(board.getStatus()));
    }

    private Board createBoard(Integer userCount)
    {
        BoardParams params = new BoardParams();
        params.setDuration(1);
        params.setName("Test Masası");
        params.setRuleId(rule.getId());
        params.setUserCount(userCount);
        params.setUserId(creator.getId());
        return boardService.create(params);
    }
}
