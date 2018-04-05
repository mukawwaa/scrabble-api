package com.gamecity.scrabble.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.Rack;
import com.gamecity.scrabble.model.RackTile;
import com.gamecity.scrabble.service.ContentService;
import com.gamecity.scrabble.service.GameService;
import com.gamecity.scrabble.service.exception.GameException;
import com.gamecity.scrabble.service.exception.error.GameError;
import com.gamecity.scrabble.util.DateUtils;

public class GameTest extends AbstractScrabbleTest
{
    private User creator;
    private User player;
    private Board board;

    @Autowired
    protected ContentService contentService;

    @Autowired
    protected GameService gameService;

    @Before
    public void before()
    {
        super.setUp();
        creator = createUser();
        player = createUser();
        createBoardAndStartGame();
    }

    @Test
    public void testCurrentUserIsOwnerWhenGameStarted()
    {
        assertEquals(creator.getId(), board.getCurrentUser().getId());
    }

    @Test
    public void testRackIsNotEmptyWhenGameStarted()
    {
        Rack rack = contentService.getRack(board.getId(), creator.getId());
        assertEquals(rack.getTiles().size(), 7);
    }

    @Test
    public void testWrongPlayerPlayed()
    {
        try
        {
            Rack rack = contentService.getRack(board.getId(), createUser().getId());
            gameService.validateCurrentPlayer(board, rack);
            assertEquals(true, false);
        }
        catch (GameException e)
        {
            assertEquals(e.getErrorCode(), GameError.NOT_YOUR_TURN.getCode());
        }
    }

    @Test
    public void testRackIsNotValidated()
    {
        try
        {
            Rack rack = contentService.getRack(board.getId(), creator.getId());
            rack.getTiles().remove(rack.getTiles().size() - 1);
            gameService.validateRack(rack);
        }
        catch (GameException e)
        {
            assertEquals(e.getErrorCode(), GameError.INVALID_RACK.getCode());
        }
    }

    @Test
    public void testTurnPassedWhenNoMovesMade()
    {
        Rack rack = contentService.getRack(board.getId(), creator.getId());
        gameService.calculateScore(board, rack, DateUtils.nowAsUnixTime());
    }

    // ------------------------------------------- waiting -------------------------------------------

    // @Test
    public void testStartingCellNotEmptyInFirstPlay()
    {
        try
        {
            Rack rack = contentService.getRack(board.getId(), creator.getId());
            gameService.play(rack);
        }
        catch (GameException e)
        {
            assertEquals(e.getErrorCode(), GameError.STARTING_CELL_CANNOT_BE_EMPTY.getCode());
        }
    }

    // @Test
    public void testCorrectMove()
    {
        try
        {
            Rack rack = createCorrectRack(board.getId(), creator.getId());
            gameService.play(rack);
            rack = contentService.getRack(board.getId(), creator.getId());
        }
        catch (GameException e)
        {
            // TODO create a correct word
            assertEquals(e.getErrorCode(), GameError.WORD_IS_NOT_DEFINED.getCode());
        }
    }

    // @Test
    public void testWrongMove()
    {
        try
        {
            Rack rack = createWrongRack(board.getId(), creator.getId());
            gameService.play(rack);
            assertEquals(true, false);
        }
        catch (GameException e)
        {
            assertEquals(e.getErrorCode(), GameError.WORD_IS_NOT_DEFINED.getCode());
        }
    }

    // @Test
    public void testRackNotEmptyWhenGameStarted()
    {
        Rack rack = contentService.getRack(board.getId(), creator.getId());
        assertTrue(rack.getTiles().size() > 0);
    }

    // @Test
    public void testRackNotEmptyWhenTurnChanged()
    {
        try
        {
            playAMove(creator);
            board = boardService.get(board.getId());
            Rack rack = contentService.getRack(board.getId(), board.getCurrentUser().getId());
            assertTrue(rack.getTiles().size() > 0);
        }
        catch (GameException e)
        {
            // TODO create a correct word
            assertEquals(e.getErrorCode(), GameError.WORD_IS_NOT_DEFINED.getCode());
        }
    }

    // ---------------------------------------------------- private methods ----------------------------------------------------

    private void createBoardAndStartGame()
    {
        board = createBoard(creator, 2);
        boardService.join(board.getId(), player.getId());
        board = boardService.get(board.getId());
        assertTrue(BoardStatus.STARTED.equals(board.getStatus()));
    }

    private void playAMove(User user)
    {
        Rack rack = createCorrectRack(board.getId(), user.getId());
        gameService.play(rack);
    }

    private Rack createCorrectRack(Long boardId, Long userId)
    {
        Rack rack = contentService.getRack(board.getId(), userId);
        int columnNumber = 5;
        for (RackTile rackTile : rack.getTiles())
        {
            rackTile.setColumnNumber(columnNumber);
            rackTile.setRowNumber(8);
            rackTile.setUsed(true);
            columnNumber = columnNumber + 1;
        }
        return rack;
    }

    private Rack createWrongRack(Long boardId, Long userId)
    {
        Rack rack = contentService.getRack(board.getId(), userId);
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
        return rack;
    }
}
