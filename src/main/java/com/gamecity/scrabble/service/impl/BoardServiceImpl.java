package com.gamecity.scrabble.service.impl;

import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamecity.scrabble.Constants;
import com.gamecity.scrabble.dao.BoardDao;
import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.entity.BoardStatus;
import com.gamecity.scrabble.entity.BoardUser;
import com.gamecity.scrabble.entity.Rule;
import com.gamecity.scrabble.entity.User;
import com.gamecity.scrabble.model.BoardContent;
import com.gamecity.scrabble.model.BoardParams;
import com.gamecity.scrabble.service.BoardService;
import com.gamecity.scrabble.service.BoardUserService;
import com.gamecity.scrabble.service.ContentService;
import com.gamecity.scrabble.service.GameService;
import com.gamecity.scrabble.service.RuleService;
import com.gamecity.scrabble.service.UserService;
import com.gamecity.scrabble.service.exception.GameError;
import com.gamecity.scrabble.service.exception.GameException;
import com.gamecity.scrabble.util.DateUtils;
import com.gamecity.scrabble.util.ValidationUtils;

@Service(value = "boardService")
public class BoardServiceImpl extends AbstractServiceImpl<Board, BoardDao> implements BoardService
{
    @Autowired
    private UserService userService;

    @Autowired
    private BoardUserService boardUserService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private GameService gameService;

    @Autowired
    private ContentService contentService;

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
        User user = userService.checkValidUser(params.getUserId());

        Board board = new Board(user, rule, params.getName(), params.getUserCount(), params.getDuration());
        createBoardUser(board, user, Constants.BoardSettings.FIRST_ROUND);
        save(board);
        contentService.updateContent(board.getId(), board.getOrderNo());

        return board;
    }

    @Override
    @Transactional
    public Integer join(Long boardId, Long userId)
    {
        ValidationUtils.validateParameters(boardId, userId);

        User user = userService.checkValidUser(userId);
        Board board = checkBoardAvailable(boardId);

        BoardUser boardUser = boardUserService.loadByUserId(boardId, userId);
        if (boardUser != null)
        {
            throw new GameException(GameError.ALREADY_ON_BOARD, boardId);
        }

        if (BoardStatus.STARTED.equals(board.getStatus()))
        {
            throw new GameException(GameError.GAME_IS_STARTED);
        }

        BoardContent content = contentService.getContent(boardId, board.getOrderNo());
        Integer userCount = content.getPlayers().size() + 1;

        createBoardUser(board, user, userCount);
        checkReady(board, userCount);
        save(board);

        return board.getOrderNo();
    }

    @Override
    @Transactional
    public Integer leave(Long boardId, Long userId)
    {
        ValidationUtils.validateParameters(boardId, userId);

        User user = userService.checkValidUser(userId);
        Board board = checkBoardAvailable(boardId);

        BoardUser boardUser = boardUserService.loadByUserId(boardId, userId);
        if (boardUser == null)
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

        removeBoardUser(boardUser, user);
        save(board);
        contentService.updateContent(boardId, board.getOrderNo());

        return board.getOrderNo();
    }

    @Override
    public List<Board> getActiveBoards()
    {
        return baseDao.getAllByStatus(EnumSet.of(BoardStatus.WAITING_USERS_TO_JOIN, BoardStatus.STARTED));
    }

    @Override
    public List<Board> getActiveBoardsByUser(Long userId)
    {
        return baseDao.getUserBoardsByStatus(userId, EnumSet.of(BoardStatus.WAITING_USERS_TO_JOIN, BoardStatus.STARTED));
    }

    @Override
    @Transactional
    public Boolean stopExpired()
    {
        baseDao.stopExpired();
        return true;
    }

    // ---------------------------------------------------- private methods ----------------------------------------------------

    private void createBoardUser(Board board, User user, Integer currentOrderNo)
    {
        BoardUser boardUser = new BoardUser(board, user, currentOrderNo);
        boardUser.setJoinDate(DateUtils.nowAsUnixTime());
        boardUserService.save(boardUser);
    }

    private void removeBoardUser(BoardUser boardUser, User user)
    {
        boardUser.setLeaveDate(DateUtils.nowAsUnixTime());
        boardUserService.save(boardUser);
    }

    private void checkReady(Board board, Integer userCount)
    {
        // start the game if all users have joined
        if (userCount == board.getUserCount())
        {
            gameService.start(board);
        }
        else
        {
            contentService.updateContent(board.getId(), board.getOrderNo());
        }
    }
}
