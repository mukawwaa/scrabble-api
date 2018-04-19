package com.gamecity.scrabble.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.gamecity.scrabble.dao.BoardDao;
import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.entity.BoardUserHistory;
import com.gamecity.scrabble.entity.PlayerAction;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.entity.cassandra.BoardUserCounter;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.BoardUserHistoryService;
import com.gamecity.scrabble.service.ContentService;
import com.gamecity.scrabble.service.GameService;
import com.gamecity.scrabble.service.RuleService;
import com.gamecity.scrabble.service.UserService;
import com.gamecity.scrabble.service.exception.BoardException;
import com.gamecity.scrabble.service.exception.error.BoardError;
import com.gamecity.scrabble.service.impl.BoardServiceImpl;

public class TestBoardService extends AbstractMockTest
{
    private User creator;

    @InjectMocks
    public BoardService boardService = new BoardServiceImpl();

    @Mock
    private BoardDao boardDao;

    @Mock
    private UserService userService;

    @Mock
    private RuleService ruleService;

    @Mock
    private ContentService contentService;

    @Mock
    private GameService gameService;

    @Mock
    private BoardUserHistoryService boardUserHistoryService;

    @Before
    public void before()
    {
        MockitoAnnotations.initMocks(boardService);
        creator = mock(User.class);
        when(userService.validateAndGetUser(eq(creator.getId()))).thenReturn(creator);
    }

    @Test
    public void testValidateUnknownBoard()
    {
        try
        {
            boardService.validateAndGetAvailableBoard(DEFAULT_BOARD_ID);
            fail("Failed to validate unknown board.");
        }
        catch (BoardException e)
        {
            assertThat(e.getErrorCode(), equalTo(BoardError.INVALID_BOARD_ID.getCode()));
        }
    }

    @Test
    public void testValidateTerminatedBoard()
    {
        try
        {
            when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenAnswer(new Answer<Board>()
            {
                @Override
                public Board answer(InvocationOnMock invocation) throws Throwable
                {
                    Board board = new Board();
                    board.setStatus(BoardStatus.TERMINATED);
                    return board;
                }
            });
            boardService.validateAndGetAvailableBoard(DEFAULT_BOARD_ID);
            fail("Failed to validate terminated board.");
        }
        catch (BoardException e)
        {
            assertThat(e.getErrorCode(), equalTo(BoardError.BOARD_IS_TERMINATED.getCode()));
        }
    }

    @Test
    public void testValidateFinishedBoard()
    {
        try
        {
            when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenAnswer(new Answer<Board>()
            {
                @Override
                public Board answer(InvocationOnMock invocation) throws Throwable
                {
                    Board board = new Board();
                    board.setStatus(BoardStatus.FINISHED);
                    return board;
                }
            });
            boardService.validateAndGetAvailableBoard(DEFAULT_BOARD_ID);
            fail("Failed to validate finished board.");
        }
        catch (BoardException e)
        {
            assertThat(e.getErrorCode(), equalTo(BoardError.BOARD_IS_FINISHED.getCode()));
        }
    }

    @Test
    public void testValidateAvailableBoard()
    {
        when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenAnswer(new Answer<Board>()
        {
            @Override
            public Board answer(InvocationOnMock invocation) throws Throwable
            {
                Board board = new Board();
                board.setStatus(BoardStatus.WAITING_PLAYERS);
                return board;
            }
        });
        assertThat(boardService.validateAndGetAvailableBoard(DEFAULT_BOARD_ID), is(notNullValue()));
    }

    @Test
    public void testValidateNotStartedBoard()
    {
        try
        {
            when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenAnswer(new Answer<Board>()
            {
                @Override
                public Board answer(InvocationOnMock invocation) throws Throwable
                {
                    Board board = new Board();
                    board.setStatus(BoardStatus.WAITING_PLAYERS);
                    return board;
                }
            });
            boardService.validateAndGetStartedBoard(DEFAULT_BOARD_ID);
            fail("Failed to validate started board.");
        }
        catch (BoardException e)
        {
            assertThat(e.getErrorCode(), equalTo(BoardError.BOARD_IS_NOT_STARTED.getCode()));
        }
    }

    @Test
    public void testValidateStartedBoard()
    {
        when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenAnswer(new Answer<Board>()
        {
            @Override
            public Board answer(InvocationOnMock invocation) throws Throwable
            {
                Board board = new Board();
                board.setStatus(BoardStatus.STARTED);
                return board;
            }
        });
        assertThat(boardService.validateAndGetStartedBoard(DEFAULT_BOARD_ID), is(notNullValue()));
    }

    @Test
    public void testValidateNotWaitingBoard()
    {
        try
        {
            when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenAnswer(new Answer<Board>()
            {
                @Override
                public Board answer(InvocationOnMock invocation) throws Throwable
                {
                    Board board = new Board();
                    board.setStatus(BoardStatus.STARTED);
                    return board;
                }
            });
            boardService.validateAndGetWaitingBoard(DEFAULT_BOARD_ID);
            fail("Failed to validate waiting board.");
        }
        catch (BoardException e)
        {
            assertThat(e.getErrorCode(), equalTo(BoardError.BOARD_IS_STARTED.getCode()));
        }
    }

    @Test
    public void testValidateWaitingBoard()
    {
        when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenAnswer(new Answer<Board>()
        {
            @Override
            public Board answer(InvocationOnMock invocation) throws Throwable
            {
                Board board = new Board();
                board.setStatus(BoardStatus.WAITING_PLAYERS);
                return board;
            }
        });
        assertThat(boardService.validateAndGetWaitingBoard(DEFAULT_BOARD_ID), is(notNullValue()));
    }

    @Test
    public void testCreateNewBoard()
    {
        int userCount = 2;
        long playerCount = 1L;
        long actionCount = playerCount;

        when(boardDao.save(any(Board.class))).thenAnswer(new Answer<Board>()
        {
            @Override
            public Board answer(InvocationOnMock invocation) throws Throwable
            {
                Board board = (Board) invocation.getArguments()[0];
                board.setId(DEFAULT_BOARD_ID);
                return board;
            }
        });
        when(boardUserHistoryService.calculatePlayerCount(eq(DEFAULT_BOARD_ID), eq(PlayerAction.CREATE)))
            .thenReturn(new BoardUserCounter(DEFAULT_BOARD_ID, playerCount, actionCount));
        Board board = boardService.create(createSampleBoard(creator.getId(), userCount));

        verify(contentService, times(1)).updateWaitingPlayers(DEFAULT_BOARD_ID, creator.getId(), Math.toIntExact(actionCount));
        assertThat(board.getId(), is(notNullValue()));
        assertThat(board.getStatus(), equalTo(BoardStatus.WAITING_PLAYERS));
    }

    @Test
    public void testJoinBoard()
    {
        int userCount = 3;
        long playerCount = 2L;
        long actionCount = playerCount;
        long secondPlayer = 1L;

        when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenReturn(createMockBoard(creator, userCount));
        when(boardUserHistoryService.calculatePlayerCount(eq(DEFAULT_BOARD_ID), eq(PlayerAction.JOIN)))
            .thenReturn(new BoardUserCounter(DEFAULT_BOARD_ID, playerCount, actionCount));

        boardService.join(DEFAULT_BOARD_ID, secondPlayer);
        verify(contentService, times(1)).updateWaitingPlayers(DEFAULT_BOARD_ID, secondPlayer, Math.toIntExact(actionCount));
    }

    @Test
    public void testJoinBoardWithAlreadyJoinedUserError()
    {
        int userCount = 3;
        long secondPlayer = 1L;

        when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenReturn(createMockBoard(creator, userCount));
        when(boardUserHistoryService.loadLastActionByUserId(eq(DEFAULT_BOARD_ID), eq(secondPlayer))).thenAnswer(new Answer<BoardUserHistory>()
        {
            @Override
            public BoardUserHistory answer(InvocationOnMock invocation) throws Throwable
            {
                BoardUserHistory boardUserHistory = new BoardUserHistory();
                boardUserHistory.setAction(PlayerAction.JOIN);
                return boardUserHistory;
            }
        });
        try
        {
            boardService.join(DEFAULT_BOARD_ID, secondPlayer);
            fail("Failed to validate joined user with already joined error.");
        }
        catch (BoardException e)
        {
            assertThat(e.getErrorCode(), equalTo(BoardError.ALREADY_ON_BOARD.getCode()));
        }
    }

    @Test
    public void testLeaveBoard()
    {
        int userCount = 3;
        long playerCount = 2L;
        long actionCount = playerCount + 1L;
        long secondPlayer = 1L;

        when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenReturn(createMockBoard(creator, userCount));
        when(boardUserHistoryService.loadLastActionByUserId(eq(DEFAULT_BOARD_ID), eq(secondPlayer))).thenAnswer(new Answer<BoardUserHistory>()
        {
            @Override
            public BoardUserHistory answer(InvocationOnMock invocation) throws Throwable
            {
                BoardUserHistory boardUserHistory = new BoardUserHistory();
                boardUserHistory.setAction(PlayerAction.JOIN);
                return boardUserHistory;
            }
        });
        when(boardUserHistoryService.calculatePlayerCount(eq(DEFAULT_BOARD_ID), eq(PlayerAction.LEFT)))
            .thenReturn(new BoardUserCounter(DEFAULT_BOARD_ID, playerCount, actionCount));

        boardService.leave(DEFAULT_BOARD_ID, secondPlayer);
        verify(contentService, times(1)).updateWaitingPlayers(DEFAULT_BOARD_ID, secondPlayer, Math.toIntExact(actionCount));
    }

    @Test
    public void testLeaveBoardWithNotJoinedUserError()
    {
        int userCount = 3;
        long secondPlayer = 1L;

        when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenReturn(createMockBoard(creator, userCount));
        try
        {
            boardService.leave(DEFAULT_BOARD_ID, secondPlayer);
            fail("Failed to validate not joined user with not joined error.");
        }
        catch (BoardException e)
        {
            assertThat(e.getErrorCode(), equalTo(BoardError.NOT_ON_BOARD.getCode()));
        }
    }

    @Test
    public void testBoardStarted()
    {
        int userCount = 2;
        long playerCount = 2L;
        long actionCount = playerCount;
        long secondPlayer = 1L;

        Board mockBoard = createMockBoard(creator, userCount);
        when(boardDao.get(eq(DEFAULT_BOARD_ID))).thenReturn(mockBoard);
        when(boardUserHistoryService.calculatePlayerCount(eq(DEFAULT_BOARD_ID), eq(PlayerAction.JOIN)))
            .thenReturn(new BoardUserCounter(DEFAULT_BOARD_ID, playerCount, actionCount));

        boardService.join(DEFAULT_BOARD_ID, secondPlayer);
        verify(gameService, times(1)).start(mockBoard);
    }
}
