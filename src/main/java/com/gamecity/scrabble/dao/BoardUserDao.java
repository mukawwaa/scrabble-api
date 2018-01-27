package com.gamecity.scrabble.dao;

import java.util.List;

import com.gamecity.scrabble.entity.BoardUser;

public interface BoardUserDao extends BaseDao<BoardUser>
{
    BoardUser loadByUserId(Long boardId, Long userId);

    BoardUser getNextUser(Long boardId, Integer orderNo);

    Integer getBoardUserCount(Long boardId);

    List<BoardUser> loadAllActiveUsers(Long boardId);
}
