package com.gamecity.scrabble.service.impl;

import java.util.EnumSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamecity.scrabble.Constants;
import com.gamecity.scrabble.dao.BoardDao;
import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.entity.BoardUserHistory;
import com.gamecity.scrabble.entity.PlayerAction;
import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.entity.cassandra.BoardUserCounter;
import com.gamecity.scrabble.model.BoardParams;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.BoardUserHistoryService;
import com.gamecity.scrabble.service.ContentService;
import com.gamecity.scrabble.service.GameService;
import com.gamecity.scrabble.service.RuleService;
import com.gamecity.scrabble.service.UserService;
import com.gamecity.scrabble.service.exception.BoardException;
import com.gamecity.scrabble.service.exception.error.BoardError;
import com.gamecity.scrabble.util.ValidationUtils;

@Service(value = "boardService")
public class BoardServiceImpl extends AbstractServiceImpl<Board, BoardDao> implements BoardService
{
    private static Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private GameService gameService;

    @Autowired
    private BoardUserHistoryService boardUserHistoryService;

    @Override
    public Board validateAndGetAvailableBoard(Long id)
    {
        Board board = boardDao.get(id);
        if (board == null)
        {
            throw new BoardException(BoardError.INVALID_BOARD_ID);
        }
        else if (BoardStatus.TERMINATED.equals(board.getStatus()))
        {
            throw new BoardException(BoardError.BOARD_IS_TERMINATED);
        }
        else if (BoardStatus.FINISHED.equals(board.getStatus()))
        {
            throw new BoardException(BoardError.BOARD_IS_FINISHED);
        }
        return board;
    }

    @Override
    public Board validateAndGetStartedBoard(Long id)
    {
        Board board = validateAndGetAvailableBoard(id);
        if (!BoardStatus.STARTED.equals(board.getStatus()))
        {
            throw new BoardException(BoardError.BOARD_IS_NOT_STARTED);
        }
        return board;
    }

    @Override
    public Board validateAndGetWaitingBoard(Long id)
    {
        Board board = validateAndGetAvailableBoard(id);
        if (BoardStatus.STARTED.equals(board.getStatus()))
        {
            throw new BoardException(BoardError.BOARD_IS_STARTED);
        }
        return board;
    }

    @Override
    @Transactional
    public Board create(BoardParams params)
    {
        Rule rule = ruleService.get(params.getRuleId());
        User user = userService.validateAndGetUser(params.getUserId());

        Board board = new Board();
        board.setCurrentUser(user);
        board.setDuration(params.getDuration());
        board.setName(params.getName());
        board.setOrderNo(Constants.BoardSettings.DEFAULT_ORDER);
        board.setOwner(user);
        board.setRule(rule);
        board.setStatus(BoardStatus.WAITING_PLAYERS);
        board.setUserCount(params.getUserCount());
        board = boardDao.save(board);

        contentService.updateContent(board.getId(), board.getOrderNo());
        createBoardUserHistory(board.getId(), PlayerAction.CREATE, user.getId());
        validateGameStatus(board, PlayerAction.CREATE, user.getId());

        return board;
    }

    @Override
    @Transactional
    public void join(Long boardId, Long userId)
    {
        ValidationUtils.validateParameters(boardId, userId);

        userService.validateAndGetUser(userId);
        Board board = validateAndGetWaitingBoard(boardId);

        BoardUserHistory boardUserHistory = boardUserHistoryService.loadLastActionByUserId(boardId, userId);
        if (boardUserHistory != null && PlayerAction.JOIN.equals(boardUserHistory.getAction()))
        {
            throw new BoardException(BoardError.ALREADY_ON_BOARD, boardId);
        }

        createBoardUserHistory(board.getId(), PlayerAction.JOIN, userId);
        validateGameStatus(board, PlayerAction.JOIN, userId);
    }

    @Override
    @Transactional
    public void leave(Long boardId, Long userId)
    {
        ValidationUtils.validateParameters(boardId, userId);

        userService.validateAndGetUser(userId);
        Board board = validateAndGetWaitingBoard(boardId);

        BoardUserHistory boardUserHistory = boardUserHistoryService.loadLastActionByUserId(boardId, userId);
        if (boardUserHistory == null || PlayerAction.LEFT.equals(boardUserHistory.getAction()))
        {
            throw new BoardException(BoardError.NOT_ON_BOARD);
        }

        if (board.getOwner().getId().equals(userId))
        {
            throw new BoardException(BoardError.OWNER_CANNOT_LEAVE_BOARD);
        }

        createBoardUserHistory(board.getId(), PlayerAction.LEFT, userId);
        validateGameStatus(board, PlayerAction.LEFT, userId);
    }

    @Override
    public List<Board> getActiveBoards()
    {
        return boardDao.getAllByStatus(EnumSet.of(BoardStatus.WAITING_PLAYERS, BoardStatus.STARTED));
    }

    @Override
    public List<Board> getActiveBoardsByUser(Long userId)
    {
        return boardDao.getUserBoardsByStatus(userId, EnumSet.of(BoardStatus.WAITING_PLAYERS, BoardStatus.STARTED));
    }

    @Override
    @Transactional
    public Boolean stopExpired()
    {
        boardDao.stopExpired();
        return true;
    }

    // ------------------------------------------------ private methods ------------------------------------------------

    private void createBoardUserHistory(Long boardId, PlayerAction action, Long userId)
    {
        BoardUserHistory boardUserHistory = new BoardUserHistory();
        boardUserHistory.setBoardId(boardId);
        boardUserHistory.setUserId(userId);
        boardUserHistory.setAction(action);
        boardUserHistoryService.save(boardUserHistory);
    }

    private void validateGameStatus(Board board, PlayerAction action, Long userId)
    {
        BoardUserCounter counter = boardUserHistoryService.calculatePlayerCount(board.getId(), action);

        if (board.getUserCount().equals(counter.getPlayerCount().intValue()))
        {
            gameService.start(board);
        }
        else if (board.getUserCount() > counter.getPlayerCount().intValue())
        {
            contentService.updateWaitingPlayers(board.getId(), userId, counter.getActionCount().intValue());
        }
        else
        {
            logger.error("User {} exceeded the limit {} of board {}.", userId, board.getUserCount(), board.getId());
            throw new BoardException(BoardError.BOARD_IS_STARTED);
        }
    }
}
