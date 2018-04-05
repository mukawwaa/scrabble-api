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
    public Board checkBoardAvailable(Long id)
    {
        Board board = get(id);
        if (board == null || BoardStatus.TERMINATED.equals(board.getStatus()) || BoardStatus.FINISHED.equals(board.getStatus()))
        {
            throw new BoardException(BoardError.INVALID_BOARD_ID);
        }
        return board;
    }

    @Override
    public Board checkBoardStarted(Long id)
    {
        Board board = checkBoardAvailable(id);
        if (!BoardStatus.STARTED.equals(board.getStatus()))
        {
            throw new BoardException(BoardError.BOARD_IS_NOT_STARTED);
        }
        return board;
    }

    @Override
    @Transactional
    public Board create(BoardParams params)
    {
        Rule rule = ruleService.get(params.getRuleId());
        User user = userService.validateUser(params.getUserId());

        Board board = new Board();
        board.setCurrentUser(user);
        board.setDuration(params.getDuration());
        board.setName(params.getName());
        board.setOrderNo(Constants.BoardSettings.DEFAULT_ORDER);
        board.setOwner(user);
        board.setRule(rule);
        board.setStatus(BoardStatus.WAITING_PLAYERS);
        board.setUserCount(params.getUserCount());
        save(board);

        contentService.updateContent(board.getId(), board.getOrderNo());
        createBoardUserHistory(board, user.getId(), PlayerAction.CREATE);

        return board;
    }

    @Override
    @Transactional
    public void join(Long boardId, Long userId)
    {
        ValidationUtils.validateParameters(boardId, userId);

        userService.validateUser(userId);
        Board board = checkBoardAvailable(boardId);

        BoardUserHistory boardUserHistory = boardUserHistoryService.loadLastActionByUserId(boardId, userId);
        if (boardUserHistory != null && PlayerAction.JOIN.equals(boardUserHistory.getAction()))
        {
            throw new BoardException(BoardError.ALREADY_ON_BOARD, boardId);
        }

        if (BoardStatus.STARTED.equals(board.getStatus()))
        {
            throw new BoardException(BoardError.BOARD_IS_STARTED);
        }

        createBoardUserHistory(board, userId, PlayerAction.JOIN);
    }

    @Override
    @Transactional
    public void leave(Long boardId, Long userId)
    {
        ValidationUtils.validateParameters(boardId, userId);

        userService.validateUser(userId);
        Board board = checkBoardAvailable(boardId);

        BoardUserHistory boardUserHistory = boardUserHistoryService.loadLastActionByUserId(boardId, userId);
        if (boardUserHistory == null || PlayerAction.LEFT.equals(boardUserHistory.getAction()))
        {
            throw new BoardException(BoardError.NOT_ON_BOARD);
        }

        if (BoardStatus.STARTED.equals(board.getStatus()))
        {
            throw new BoardException(BoardError.BOARD_IS_STARTED);
        }

        if (board.getOwner().getId().equals(userId))
        {
            throw new BoardException(BoardError.OWNER_CANNOT_LEAVE_BOARD);
        }

        createBoardUserHistory(board, userId, PlayerAction.LEFT);
    }

    @Override
    public List<Board> getActiveBoards()
    {
        return baseDao.getAllByStatus(EnumSet.of(BoardStatus.WAITING_PLAYERS, BoardStatus.STARTED));
    }

    @Override
    public List<Board> getActiveBoardsByUser(Long userId)
    {
        return baseDao.getUserBoardsByStatus(userId, EnumSet.of(BoardStatus.WAITING_PLAYERS, BoardStatus.STARTED));
    }

    @Override
    @Transactional
    public Boolean stopExpired()
    {
        baseDao.stopExpired();
        return true;
    }

    // ------------------------------------------------ private methods ------------------------------------------------

    private void createBoardUserHistory(Board board, Long userId, PlayerAction action)
    {
        BoardUserHistory boardUserHistory = new BoardUserHistory();
        boardUserHistory.setBoardId(board.getId());
        boardUserHistory.setUserId(userId);
        boardUserHistory.setAction(action);
        boardUserHistoryService.save(boardUserHistory);

        validateGameStatus(board, boardUserHistory);
    }

    private void validateGameStatus(Board board, BoardUserHistory boardUserHistory)
    {
        BoardUserCounter counter = boardUserHistoryService.calculatePlayerCount(board.getId(), boardUserHistory.getAction());

        if (board.getUserCount().equals(counter.getPlayerCount().intValue()))
        {
            gameService.start(board);
        }
        else if (board.getUserCount() > counter.getPlayerCount().intValue())
        {
            contentService.updateWaitingPlayers(board.getId(), counter.getActionCount().intValue(), boardUserHistory);
        }
        else
        {
            logger.warn("User {} exceeded the limit {} of board {}.", boardUserHistory.getUserId(), board.getUserCount(), board.getId());
            throw new BoardException(BoardError.BOARD_IS_STARTED);
        }
    }
}
