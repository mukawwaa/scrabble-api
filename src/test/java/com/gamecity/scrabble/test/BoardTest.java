package com.gamecity.scrabble.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.service.BoardUserHistoryService;
import com.gamecity.scrabble.service.exception.BoardException;
import com.gamecity.scrabble.service.exception.error.BoardError;

public class BoardTest extends AbstractScrabbleTest
{
    private User creator;

    @Autowired
    private BoardUserHistoryService boardUserHistoryService;

    @Before
    public void before()
    {
        super.setUp();
        creator = createUser();
    }

    @Test
    public void testCreateNewBoard()
    {
        Board board = createBoard(creator, 2);
        assertNotNull(board.getId());
        assertEquals(BoardStatus.WAITING_PLAYERS, board.getStatus());
    }

    @Test
    public void testJoinBoard()
    {
        Board board = createBoard(creator, 3);
        boardService.join(board.getId(), createUser().getId());
        board = boardService.get(board.getId());
        assertEquals(BoardStatus.WAITING_PLAYERS, board.getStatus());
    }

    @Test
    public void testJoinBoardWithAlreadyJoinedUserError()
    {
        Board board = createBoard(creator, 3);
        User secondPlayer = createUser();
        boardService.join(board.getId(), secondPlayer.getId());
        try
        {
            boardService.join(board.getId(), secondPlayer.getId());
            assertFalse(true);
        }
        catch (BoardException e)
        {
            assertEquals(e.getErrorCode(), BoardError.ALREADY_ON_BOARD.getCode());
        }
    }

    @Test
    public void testLeaveBoard()
    {
        Board board = createBoard(creator, 3);
        User secondPlayer = createUser();
        boardService.join(board.getId(), secondPlayer.getId());
        boardService.leave(board.getId(), secondPlayer.getId());
        board = boardService.get(board.getId());
        assertEquals(BoardStatus.WAITING_PLAYERS, board.getStatus());
    }

    @Test
    public void testLeaveBoardWithNotJoinedUserError()
    {
        Board board = createBoard(creator, 3);
        try
        {
            boardService.leave(board.getId(), createUser().getId());
            assertFalse(true);
        }
        catch (BoardException e)
        {
            assertEquals(e.getErrorCode(), BoardError.NOT_ON_BOARD.getCode());
        }
    }

    @Test
    public void testBoardUserCountIncrease()
    {
        Board board = createBoard(creator, 4);
        boardService.join(board.getId(), createUser().getId());
        assertEquals((Integer) 2, boardUserHistoryService.getWaitingUserCount(board.getId()));
        boardService.join(board.getId(), createUser().getId());
        assertEquals((Integer) 3, boardUserHistoryService.getWaitingUserCount(board.getId()));
        board = boardService.get(board.getId());
        assertEquals(BoardStatus.WAITING_PLAYERS, board.getStatus());
    }

    @Test
    public void testBoardUserCountDecrease()
    {
        Board board = createBoard(creator, 4);
        User secondPlayer = createUser();
        boardService.join(board.getId(), secondPlayer.getId());
        User thirdPlayer = createUser();
        boardService.join(board.getId(), thirdPlayer.getId());
        assertEquals((Integer) 3, boardUserHistoryService.getWaitingUserCount(board.getId()));
        boardService.leave(board.getId(), secondPlayer.getId());
        assertEquals((Integer) 2, boardUserHistoryService.getWaitingUserCount(board.getId()));
        boardService.leave(board.getId(), thirdPlayer.getId());
        assertEquals((Integer) 1, boardUserHistoryService.getWaitingUserCount(board.getId()));
        board = boardService.get(board.getId());
        assertEquals(BoardStatus.WAITING_PLAYERS, board.getStatus());
    }

    @Test
    public void testBoardStarted()
    {
        Board board = createBoard(creator, 2);
        boardService.join(board.getId(), createUser().getId());
        board = boardService.get(board.getId());
        assertTrue(BoardStatus.STARTED.equals(board.getStatus()));
    }
}
