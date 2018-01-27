package com.gamecity.scrabble.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gamecity.scrabble.model.Rack;
import com.gamecity.scrabble.service.exception.GameError;
import com.gamecity.scrabble.service.exception.GameException;

public class GameTest extends AbstractGameTest
{
    @Test
    public void testCorrectMove()
    {
        Rack rack = createCorrectRack(board.getId(), creator.getId());
        System.out.print("Rack before move -> ");
        System.out.println(rack.getTiles().toString());
        gameService.play(rack);
        rack = contentService.getRack(board.getId(), creator.getId());
        System.out.print("Rack after move -> ");
        System.out.println(rack.getTiles().toString());
    }

    @Test
    public void testWrongTurn()
    {
        try
        {
            Rack rack = createCorrectRack(board.getId(), player.getId());
            gameService.play(rack);
            assertEquals(true, false);
        }
        catch (GameException e)
        {
            assertEquals(e.getErrorCode(), GameError.NOT_YOUR_TURN.getCode());
        }
    }

    @Test
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

    @Test
    public void testRackNotEmptyWhenGameStarted()
    {
        Rack rack = contentService.getRack(board.getId(), creator.getId());
        assertTrue(rack.getTiles().size() > 0);
        System.out.println(rack.getTiles().toString());
    }

    @Test
    public void testRackNotEmptyWhenTurnChanged()
    {
        playAMove();
        board = boardService.get(board.getId());
        Rack rack = contentService.getRack(board.getId(), board.getCurrentUser().getId());
        assertTrue(rack.getTiles().size() > 0);
        System.out.println(rack.getTiles().toString());
    }

}
