package com.gamecity.scrabble.service.impl;

import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamecity.scrabble.Constants;
import com.gamecity.scrabble.dao.BoardDao;
import com.gamecity.scrabble.dao.RedisRepository;
import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.entity.BoardUserHistory;
import com.gamecity.scrabble.entity.PlayerStatus;
import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.BoardParams;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.BoardUserHistoryService;
import com.gamecity.scrabble.service.ContentService;
import com.gamecity.scrabble.service.GameService;
import com.gamecity.scrabble.service.RuleService;
import com.gamecity.scrabble.service.UserService;
import com.gamecity.scrabble.service.exception.GameError;
import com.gamecity.scrabble.service.exception.GameException;
import com.gamecity.scrabble.util.ValidationUtils;

@Service(value = "boardService")
public class BoardServiceImpl extends AbstractServiceImpl<Board, BoardDao> implements BoardService
{
    @Autowired
    private UserService userService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private GameService gameService;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private BoardUserHistoryService boardUserHistoryService;

    @Override
    public Board checkBoardAvailable(Long id)
    {
        Board board = get(id);
        if (board == null || BoardStatus.TERMINATED.equals(board.getStatus()) || BoardStatus.FINISHED.equals(board.getStatus()))
        {
            throw new GameException(GameError.INVALID_BOARD_ID);
        }
        return board;
    }

    @Override
    public Board checkBoardStarted(Long id)
    {
        Board board = checkBoardAvailable(id);
        if (!BoardStatus.STARTED.equals(board.getStatus()))
        {
            throw new GameException(GameError.GAME_IS_NOT_STARTED);
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

        createBoardUserHistory(board.getId(), user.getId(), PlayerStatus.JOINED);

        return board;
    }

    @Override
    @Transactional
    public void join(Long boardId, Long userId)
    {
        ValidationUtils.validateParameters(boardId, userId);

        userService.validateUser(userId);
        Board board = checkBoardAvailable(boardId);

        BoardUserHistory boardUserHistory = boardUserHistoryService.loadByUserId(boardId, userId);
        if (boardUserHistory != null && PlayerStatus.JOINED.equals(boardUserHistory.getStatus()))
        {
            throw new GameException(GameError.ALREADY_ON_BOARD, boardId);
        }

        if (BoardStatus.STARTED.equals(board.getStatus()))
        {
            throw new GameException(GameError.GAME_IS_STARTED);
        }

        createBoardUserHistory(boardId, userId, PlayerStatus.JOINED);
    }

    @Override
    @Transactional
    public void leave(Long boardId, Long userId)
    {
        ValidationUtils.validateParameters(boardId, userId);

        userService.validateUser(userId);
        Board board = checkBoardAvailable(boardId);

        BoardUserHistory boardUserHistory = boardUserHistoryService.loadByUserId(boardId, userId);
        if (boardUserHistory == null || PlayerStatus.LEFT.equals(boardUserHistory.getStatus()))
        {
            throw new GameException(GameError.NOT_ON_BOARD);
        }

        if (BoardStatus.STARTED.equals(board.getStatus()))
        {
            throw new GameException(GameError.GAME_IS_STARTED);
        }

        if (board.getOwner().getId().equals(userId))
        {
            throw new GameException(GameError.OWNER_CANNOT_LEAVE_BOARD);
        }

        createBoardUserHistory(boardId, userId, PlayerStatus.LEFT);
    }

    @Override
    @Transactional
    public void updateBoardUser(BoardUserHistory boardUserHistory)
    {
        Board board = get(boardUserHistory.getBoardId());

        Integer userCount = boardUserHistoryService.getWaitingUserCount(boardUserHistory.getBoardId());
        if (PlayerStatus.LEFT.equals(boardUserHistory.getStatus()))
        {
            boardUserHistory = boardUserHistoryService.loadByUserId(boardUserHistory.getBoardId(), boardUserHistory.getUserId());
            boardUserHistoryService.remove(boardUserHistory.getId());
            userCount = userCount - 1;
        }

        if (board.getUserCount().equals(userCount))
        {
            gameService.start(board);
        }
        else
        {
            contentService.updatePlayers(boardUserHistory.getBoardId(), board.getOrderNo(), false);
        }
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

    private void createBoardUserHistory(Long boardId, Long userId, PlayerStatus status)
    {
        BoardUserHistory boardUserHistory = new BoardUserHistory();
        boardUserHistory.setBoardId(boardId);
        boardUserHistory.setUserId(userId);
        boardUserHistory.setStatus(status);
        redisRepository.updateBoardUserHistory(boardUserHistory);
        if (PlayerStatus.JOINED.equals(status))
        {
            boardUserHistoryService.save(boardUserHistory);
        }
    }
}
