package com.gamecity.scrabble.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.exception.GameError;
import com.gamecity.scrabble.service.exception.GameException;

public class BoardTest extends AbstractGameTest
{
    @Autowired
    private BoardService boardService;

    @Before
    public void before()
    {
        super.setUp();
    }

    @Test
    public void testCreateNewBoard()
    {
        createBoard(creator, 2);
        assertNotNull(board.getId());
        assertEquals(BoardStatus.WAITING_PLAYERS, board.getStatus());
    }

    @Test
    public void testJoinBoard()
    {
        createBoard(creator, 3);
        boardService.join(board.getId(), player.getId());
        sleep(2);
        board = boardService.get(board.getId());
        assertEquals(BoardStatus.WAITING_PLAYERS, board.getStatus());
    }

    @Test
    public void testJoinBoardWithAlreadyJoinedUserError()
    {
        createBoard(creator, 3);
        boardService.join(board.getId(), player.getId());
        try
        {
            boardService.join(board.getId(), player.getId());
            assertFalse(true);
        }
        catch (GameException e)
        {
            assertEquals(e.getErrorCode(), GameError.ALREADY_ON_BOARD.getCode());
        }
    }

    @Test
    public void testLeaveBoard()
    {
        createBoard(creator, 3);
        boardService.join(board.getId(), player.getId());
        sleep(2);
        boardService.leave(board.getId(), player.getId());
        sleep(2);
        board = boardService.get(board.getId());
        assertEquals(BoardStatus.WAITING_PLAYERS, board.getStatus());
    }

    @Test
    public void testLeaveBoardWithNotJoinedUserError()
    {
        createBoard(creator, 3);
        try
        {
            boardService.leave(board.getId(), player.getId());
            assertFalse(true);
        }
        catch (GameException e)
        {
            assertEquals(e.getErrorCode(), GameError.NOT_ON_BOARD.getCode());
        }
    }

    @Test
    public void testBoardUserCountIncrease()
    {
        createBoard(creator, 4);
        boardService.join(board.getId(), player.getId());
        sleep(2);
        assertEquals((Integer) 2, boardUserHistoryService.getWaitingUserCount(board.getId()));
        boardService.join(board.getId(), admin.getId());
        sleep(2);
        assertEquals((Integer) 3, boardUserHistoryService.getWaitingUserCount(board.getId()));
        board = boardService.get(board.getId());
        assertEquals(BoardStatus.WAITING_PLAYERS, board.getStatus());
    }

    @Test
    public void testBoardUserCountDecrease()
    {
        createBoard(creator, 4);
        boardService.join(board.getId(), player.getId());
        boardService.join(board.getId(), admin.getId());
        sleep(2);
        assertEquals((Integer) 3, boardUserHistoryService.getWaitingUserCount(board.getId()));
        boardService.leave(board.getId(), player.getId());
        sleep(2);
        assertEquals((Integer) 2, boardUserHistoryService.getWaitingUserCount(board.getId()));
        boardService.leave(board.getId(), admin.getId());
        sleep(2);
        assertEquals((Integer) 1, boardUserHistoryService.getWaitingUserCount(board.getId()));
        board = boardService.get(board.getId());
        assertEquals(BoardStatus.WAITING_PLAYERS, board.getStatus());
    }

    @Test
    public void testBoardStarted()
    {
        createBoard(creator, 2);
        boardService.join(board.getId(), player.getId());
        sleep(2);
        board = boardService.get(board.getId());
        assertTrue(BoardStatus.STARTED.equals(board.getStatus()));
    }
}
