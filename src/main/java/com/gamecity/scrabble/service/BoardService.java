package com.gamecity.scrabble.service;

import java.util.List;

import com.gamecity.scrabble.entity.Board;
import com.gamecity.scrabble.model.BoardParams;

public interface BoardService extends BaseService<Board>
{
    Board checkBoardAvailable(Long boardId);

    Board checkBoardStarted(Long boardId);

    Board create(BoardParams params);

    void join(Long boardId, Long userId);

    void leave(Long boardId, Long userId);

    void createBoardUser(Long boardId, Long userId);

    void removeBoardUser(Long boardId, Long userId);

    List<Board> getActiveBoards();

    List<Board> getActiveBoardsByUser(Long userId);

    Boolean stopExpired();
}
