package com.gamecity.scrabble.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.gamecity.scrabble.model.Rack;
import com.gamecity.scrabble.service.exception.GameError;
import com.gamecity.scrabble.service.exception.GameException;
import com.gamecity.scrabble.util.DateUtils;

public class GameTest extends AbstractGameTest
{
    @Before
    public void before()
    {
        super.setUp();
        createBoardAndStartGame(creator);
    }

    @Test
    public void testCurrentUserIsOwnerWhenGameStarted()
    {
        assertEquals(creator.getId(), board.getCurrentUser().getId());
    }

    @Test
    public void testRackIsNotEmptyWhenGameStarted()
    {
        fillRack(creator.getId());
        assertEquals(rack.getTiles().size(), 7);
    }

    @Test
    public void testWrongPlayerPlayed()
    {
        try
        {
            fillRack(player.getId());
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
            fillRack(creator.getId());
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
        fillRack(creator.getId());
        gameService.calculateScore(board, rack, DateUtils.nowAsUnixTime());
    }

    // ------------------------------------------- waiting -------------------------------------------

//    @Test
    public void testStartingCellNotEmptyInFirstPlay()
    {
        try
        {
            fillRack(creator.getId());
            play();
        }
        catch (GameException e)
        {
            assertEquals(e.getErrorCode(), GameError.STARTING_CELL_CANNOT_BE_EMPTY.getCode());
        }
    }

//    @Test
    public void testCorrectMove()
    {
        try
        {
            createCorrectRack(board.getId(), creator.getId());
            gameService.play(rack);
            rack = contentService.getRack(board.getId(), creator.getId());
        }
        catch (GameException e)
        {
            // TODO create a correct word
            assertEquals(e.getErrorCode(), GameError.WORD_IS_NOT_DEFINED.getCode());
        }
    }

//    @Test
    public void testWrongMove()
    {
        try
        {
            createWrongRack(board.getId(), creator.getId());
            gameService.play(rack);
            assertEquals(true, false);
        }
        catch (GameException e)
        {
            assertEquals(e.getErrorCode(), GameError.WORD_IS_NOT_DEFINED.getCode());
        }
    }

//    @Test
    public void testRackNotEmptyWhenGameStarted()
    {
        Rack rack = contentService.getRack(board.getId(), creator.getId());
        assertTrue(rack.getTiles().size() > 0);
    }

//    @Test
    public void testRackNotEmptyWhenTurnChanged()
    {
        try
        {
            playAMove();
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

}
