package com.gamecity.scrabble.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.BoardParams;
import com.gamecity.scrabble.model.Rack;
import com.gamecity.scrabble.model.RackTile;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.BoardUserService;
import com.gamecity.scrabble.service.ContentService;
import com.gamecity.scrabble.service.GameService;

public abstract class AbstractGameTest extends AbstractServiceTest
{
    protected Board board;
    protected User creator;
    protected User player;
    protected Rule rule;

    @Autowired
    protected BoardService boardService;

    @Autowired
    protected BoardUserService boardUserService;

    @Autowired
    protected ContentService contentService;

    @Autowired
    protected GameService gameService;

    @Before
    public void init()
    {
        creator = getCreator();
        rule = getDefaultRule();
        player = getPlayer();
        createBoardAndStartGame();
    }

    private void createBoardAndStartGame()
    {
        board = createBoard(2);
        boardService.join(board.getId(), player.getId());
        board = boardService.get(board.getId());
        assertTrue(BoardStatus.STARTED.equals(board.getStatus()));
    }

    protected void playAMove()
    {
        Rack rack = createCorrectRack(board.getId(), creator.getId());
        gameService.play(rack);
    }

    protected Board createBoard(Integer userCount)
    {
        BoardParams params = new BoardParams();
        params.setDuration(1);
        params.setName("Test Masası");
        params.setRuleId(rule.getId());
        params.setUserCount(userCount);
        params.setUserId(creator.getId());
        return boardService.create(params);
    }

    protected Rack createCorrectRack(Long boardId, Long userId)
    {
        Rack correctRack = new Rack();
        correctRack.setBoardId(boardId);
        correctRack.setUserId(userId);
        correctRack.setTiles(new ArrayList<RackTile>());
//        correctRack.getTiles().add(new RackTile(1, "S", 1, 1, Boolean.TRUE));
//        correctRack.getTiles().add(new RackTile(2, "I", 1, 2, Boolean.TRUE));
//        correctRack.getTiles().add(new RackTile(3, "Ğ", 1, 3, Boolean.TRUE));
//        correctRack.getTiles().add(new RackTile(4, "Ü", 2, 1, Boolean.TRUE));
//        correctRack.getTiles().add(new RackTile(5, "T", 3, 1, Boolean.TRUE));
//        correctRack.getTiles().add(new RackTile(6, "C"));
//        correctRack.getTiles().add(new RackTile(7, "D"));
        return correctRack;
    }

    protected Rack createWrongRack(Long boardId, Long userId)
    {
        Rack correctRack = new Rack();
        correctRack.setBoardId(boardId);
        correctRack.setUserId(userId);
        correctRack.setTiles(new ArrayList<RackTile>());
//        correctRack.getTiles().add(new RackTile(1, "G"));
//        correctRack.getTiles().add(new RackTile(2, "A", 1, 4, Boolean.TRUE));
//        correctRack.getTiles().add(new RackTile(3, "S"));
//        correctRack.getTiles().add(new RackTile(4, "N", 1, 5, Boolean.TRUE));
//        correctRack.getTiles().add(new RackTile(5, "U", 1, 1, Boolean.TRUE));
//        correctRack.getTiles().add(new RackTile(6, "R", 1, 2, Boolean.TRUE));
//        correctRack.getTiles().add(new RackTile(7, "Ç", 1, 3, Boolean.TRUE));
        return correctRack;
    }
}
