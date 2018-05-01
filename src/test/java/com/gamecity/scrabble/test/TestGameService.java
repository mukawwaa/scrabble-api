package com.gamecity.scrabble.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.PlayHelper;
import com.gamecity.scrabble.model.Rack;
import com.gamecity.scrabble.model.RackTile;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.BoardUserHistoryService;
import com.gamecity.scrabble.service.BoardUserService;
import com.gamecity.scrabble.service.ContentService;
import com.gamecity.scrabble.service.DictionaryService;
import com.gamecity.scrabble.service.GameService;
import com.gamecity.scrabble.service.UserService;
import com.gamecity.scrabble.service.WordLogService;
import com.gamecity.scrabble.service.exception.GameException;
import com.gamecity.scrabble.service.exception.error.GameError;
import com.gamecity.scrabble.service.impl.GameServiceImpl;
import com.gamecity.scrabble.util.DateUtils;

public class TestGameService extends AbstractMockTest
{
    private User creator;
    private User player;
    private Board board;

    @InjectMocks
    public GameService gameService = new GameServiceImpl();

    @Mock
    private BoardService boardService;

    @Mock
    private BoardUserService boardUserService;

    @Mock
    private BoardUserHistoryService boardUserHistoryService;

    @Mock
    private ContentService contentService;

    @Mock
    private DictionaryService dictionaryService;

    @Mock
    private WordLogService wordLogService;

    @Mock
    private UserService userService;

    @Before
    public void before()
    {
        MockitoAnnotations.initMocks(gameService);
        creator = createMockUser(1L);
        player = createMockUser(2L);
    }

    @Test
    public void testStartGame()
    {
        Board board = createMockBoard(creator, 2);
        when(boardService.save(eq(board))).thenAnswer(new Answer<Board>()
        {
            @Override
            public Board answer(InvocationOnMock invocation) throws Throwable
            {
                Board board = invocation.getArgument(0);
                board.setId(DEFAULT_BOARD_ID);
                return board;
            }
        });
        gameService.start(board);
    }

    @Test
    public void testValidateCurrentPlayerIsCorrect()
    {
        gameService.validateCurrentPlayer(creator, creator.getId());
    }

    @Test
    public void testValidateCurrentPlayerIsNotCorrect()
    {
        try
        {
            gameService.validateCurrentPlayer(creator, player.getId());
            fail("failed to validate current player is not correct.");
        }
        catch (GameException e)
        {
            assertThat(e.getErrorCode(), equalTo(GameError.NOT_YOUR_TURN.getCode()));
        }
    }

    @Test
    public void testRackIsNotValidated()
    {
        Rack rack = mock(Rack.class);
        try
        {
            when(contentService.getRack(rack.getBoardId(), rack.getUserId())).thenReturn(createMockRack(creator.getId()));
            gameService.validateRack(rack);
            fail("failed to validate wrong rack.");
        }
        catch (GameException e)
        {
            assertThat(e.getErrorCode(), equalTo(GameError.INVALID_RACK.getCode()));
        }
    }

    @Test
    public void testRackIsValidated()
    {
        Rack rack = createMockRack(creator.getId());
        when(contentService.getRack(eq(rack.getBoardId()), eq(rack.getUserId()))).thenReturn(createMockRack(creator.getId()));
        gameService.validateRack(rack);
    }

    @Test
    public void testLettersAreLocatedToBoard()
    {
        PlayHelper helper = new PlayHelper(createMockBoard(creator, 2));

        RackTile tile1 = new RackTile(1, "A", 2);
        tile1.setRowNumber(1);
        tile1.setColumnNumber(1);
        tile1.setUsed(true);

        RackTile tile2 = new RackTile(2, "B", 3);
        tile2.setRowNumber(2);
        tile2.setColumnNumber(2);
        tile2.setUsed(false);

        Rack rack = new Rack();
        rack.setTiles(new ArrayList<>());
        rack.getTiles().add(tile1);
        rack.getTiles().add(tile2);
        
        gameService.locateRackTiles(helper, rack);
        assertThat(helper.getCells()[0][0], is(notNullValue()));
        assertThat(helper.getCells()[1][1], is(nullValue()));
    }

    @Test
    public void testFindWords()
    {
        PlayHelper helper = new PlayHelper(createMockBoard(creator, 2));
        gameService.findWords(helper);
        assertThat(helper.getWords().size(), not(0));
    }

    // ------------------------------------------- waiting -------------------------------------------

    // @Test
    public void testTurnPassedWhenNoMovesMade()
    {
        Rack rack = contentService.getRack(board.getId(), creator.getId());
        gameService.calculateScore(board, rack, DateUtils.nowAsUnixTime());
    }

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
            assertThat(e.getErrorCode(), equalTo(GameError.STARTING_CELL_CANNOT_BE_EMPTY.getCode()));
        }
    }

    // @Test
    public void testCorrectMove()
    {
        try
        {
//            Rack rack = createCorrectRack(board.getId(), creator.getId());
//            gameService.play(rack);
//            rack = contentService.getRack(board.getId(), creator.getId());
        }
        catch (GameException e)
        {
            // TODO create a correct word
            assertThat(e.getErrorCode(), equalTo(GameError.WORD_IS_NOT_DEFINED.getCode()));
        }
    }

    // @Test
    public void testWrongMove()
    {
        try
        {
//            Rack rack = createWrongRack(board.getId(), creator.getId());
//            gameService.play(rack);
        }
        catch (GameException e)
        {
            assertThat(e.getErrorCode(), equalTo(GameError.WORD_IS_NOT_DEFINED.getCode()));
        }
    }

    // @Test
    public void testRackNotEmptyWhenGameStarted()
    {
        Rack rack = contentService.getRack(board.getId(), creator.getId());
        assertThat(rack.getTiles().size(), not(0));
    }

    // @Test
    public void testRackNotEmptyWhenTurnChanged()
    {
        try
        {
            playAMove(creator);
            // board = boardService.get(board.getId());
            Rack rack = contentService.getRack(board.getId(), board.getCurrentUser().getId());
            assertThat(rack.getTiles().size(), not(0));
        }
        catch (GameException e)
        {
            // TODO create a correct word
            assertThat(e.getErrorCode(), equalTo(GameError.WORD_IS_NOT_DEFINED.getCode()));
        }
    }

    // ---------------------------------------------------- private methods ----------------------------------------------------

    private void playAMove(User user)
    {
//        Rack rack = createCorrectRack(board.getId(), user.getId());
//        gameService.play(rack);
    }
}
